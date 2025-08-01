package steve6472.moondust.widget.component;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve6472
 * Date: 7/13/2025
 * Project: MoonDust <br>
 */
public interface IBounds
{
    /* TODO: IBounds changes
     *
     * Keep the Constant but replace the "Per" and "PerMath" with just an Eval type.
     * Just simple math expression evaluator with min & max functions.
     * Any "x%" number will grab the parents width and do the current percentage math
     *
     */

    Val width();
    Val height();

//    Codec<Val> VAL_CODEC = Codec.withAlternative(Con.CODEC_VAL, Per.CODEC_VAL);
    Codec<Val> VAL_CODEC = new MultiEitherCodecEncode(Con.CODEC_VAL, Per.CODEC_VAL, PerMath.CODEC_VAL);

    Codec<Pair<Val, Val>> VEC_2 = VAL_CODEC.listOf().comapFlatMap(list -> {
        if (list.size() != 2)
            return DataResult.error(() -> "List size incorrect, has to be 2");
        return DataResult.success(new Pair<>(list.get(0), list.get(1)));
    }, vec2 -> List.of(vec2.getFirst(), vec2.getSecond()));

    @FunctionalInterface
    interface Val
    {
        int calc(int parentSize);
    }

    class Con implements Val
    {
        public int value;

        public static final Codec<Con> CODEC = Codec.INT.xmap(Con::new, c -> c.value);
        public static final Codec<Val> CODEC_VAL = CODEC.flatComapMap(v -> v, v ->
        {
            if (v instanceof Con con)
                return DataResult.success(con);
            return DataResult.error(() -> "Not a Con (" + v + ")");
        });

        public Con(int value)
        {
            this.value = value;
        }

        @Override
        public int calc(int parentSize)
        {
            return value;
        }

        @Override
        public String toString()
        {
            return "Constant{" + value + '}';
        }
    }

    class Per implements Val
    {
        public double percentage;

        public static final Codec<Per> CODEC = Codec.STRING.comapFlatMap(s -> {
            if (!s.endsWith("%"))
                return DataResult.error(() -> "Percentage does not end with %");
            s = s.substring(0, s.length() - 1);

            try
            {
                double v = Double.parseDouble(s);
                return DataResult.success(new Per(v));
            } catch (NumberFormatException nfe)
            {
                return DataResult.error(nfe::getLocalizedMessage);
            }

        }, p -> p.percentage + "%");

        public static final Codec<Val> CODEC_VAL = CODEC.flatComapMap(v -> v, v ->
        {
            if (v instanceof Per per)
                return DataResult.success(per);
            return DataResult.error(() -> "Not a Per (" + v + ")");
        });

        public Per(double percentage)
        {
            this.percentage = percentage;
        }

        @Override
        public int calc(int parentSize)
        {
            return (int) (parentSize * (percentage / 100d));
        }

        @Override
        public String toString()
        {
            return "Percentage{" + percentage + "%}";
        }
    }

    class PerMath implements Val
    {
        public double percentage;
        public Sign sign;
        public int num;
        private static final Pattern PATTERN = Pattern.compile("^(\\d+(?:\\.\\d+)?)%\\s*([+-])\\s*(\\d+)$");

        public enum Sign
        {
            SUB('-'), ADD('+');

            public final char sign;
            Sign(char sign)
            {
                this.sign = sign;
            }
        }

        public static final Codec<PerMath> CODEC = Codec.STRING.comapFlatMap(s -> {
            Matcher matcher = PATTERN.matcher(s);

            if (!matcher.matches()) {
                return DataResult.error(() -> "String format must be '<double>% <+|-> <int>'");
            }

            try {
                double percentage = Double.parseDouble(matcher.group(1));
                char signChar = matcher.group(2).charAt(0);
                int num = Integer.parseInt(matcher.group(3));

                PerMath.Sign sign = Arrays.stream(PerMath.Sign.values())
                    .filter(sig -> sig.sign == signChar)
                    .findFirst()
                    .orElse(null);

                if (sign == null) {
                    return DataResult.error(() -> "Invalid sign: " + signChar);
                }

                return DataResult.success(new PerMath(percentage, sign, num));

            } catch (NumberFormatException e) {
                return DataResult.error(e::getLocalizedMessage);
            }

        }, p -> p.percentage + "% " + p.sign.sign + " " + p.num);

        public static final Codec<Val> CODEC_VAL = CODEC.flatComapMap(v -> v, v ->
        {
            if (v instanceof PerMath per)
                return DataResult.success(per);
            return DataResult.error(() -> "Not a PerMath (" + v + ")");
        });

        public PerMath(double percentage, Sign sign, int num)
        {
            this.percentage = percentage;
            this.sign = sign;
            this.num = num;
        }

        @Override
        public int calc(int parentSize)
        {
            return (int) (parentSize * (percentage / 100d)) + (sign == Sign.ADD ? num : -num);
        }

        @Override
        public String toString()
        {
            return "PercentageNum{" + percentage + "%" + " " + sign.sign + " " + num + "}";
        }
    }

    record MultiEitherCodecEncode(Codec<Val>... codecs) implements Codec<Val> {
        @SafeVarargs
        public MultiEitherCodecEncode {}

        @Override
        public <T> DataResult<Pair<Val, T>> decode(DynamicOps<T> ops, T input)
        {
            //noinspection rawtypes
            DataResult[] results = new DataResult[codecs.length];
            for (int i = 0; i < codecs.length; i++)
            {
                Codec<Val> codec = codecs[i];
                DataResult<Pair<Val, T>> decode = codec.decode(ops, input);
                if (decode.isSuccess())
                    return decode;
                results[i] = decode;
            }

            //noinspection unchecked
            for (DataResult<Pair<Val, T>> result : results)
            {
                if (result.hasResultOrPartial())
                    return result;
            }

            return DataResult.error(() ->
            {
                StringBuilder sb = new StringBuilder();
                //noinspection unchecked
                for (DataResult<T> result : results)
                {
                    sb.append(result.error().orElseThrow().message()).append(" ;");
                }
                sb.setLength(sb.length() - 2);
                return "Failed to parse all following: " + sb;
            });
        }

        private <T, V> DataResult<T> encodeValue(V input, DynamicOps<T> ops, T prefix)
        {
            for (Codec<Val> codec : codecs)
            {
                DataResult<T> encode = codec.encode((Val) input, ops, prefix);
                if (encode.isSuccess())
                    return encode;
            }
            return DataResult.error(() -> "No codec worked for encoding");
        }

        @Override
        public <T> DataResult<T> encode(Val input, DynamicOps<T> ops, T prefix)
        {
            return encodeValue(input, ops, prefix);
        }
    }
}
