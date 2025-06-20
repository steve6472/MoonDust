package steve6472.moondust.blueprints.values;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.blueprints.BlueprintValue;
import steve6472.moondust.blueprints.BlueprintValueType;
import steve6472.moondust.blueprints.ValidationResult;

public record BlueprintValueDouble(double min, double max, Double defaultValue, boolean required) implements BlueprintValue<Double>
{
    public static final Codec<BlueprintValueDouble> CODEC_DEFAULT = RecordCodecBuilder.create(instance -> instance.group(
        Codec.DOUBLE.optionalFieldOf("min", Double.MIN_VALUE).forGetter(BlueprintValueDouble::min),
        Codec.DOUBLE.optionalFieldOf("max", Double.MAX_VALUE).forGetter(BlueprintValueDouble::max),
        Codec.DOUBLE.optionalFieldOf("default", 0d).forGetter(BlueprintValueDouble::defaultValue)
    ).apply(instance, (min, max, def) -> new BlueprintValueDouble(min, max, def, false)));

    public static final Codec<BlueprintValueDouble> CODEC_REQUIRED = RecordCodecBuilder.create(instance -> instance.group(
        Codec.DOUBLE.optionalFieldOf("min", Double.MIN_VALUE).forGetter(BlueprintValueDouble::min),
        Codec.DOUBLE.optionalFieldOf("max", Double.MAX_VALUE).forGetter(BlueprintValueDouble::max),
        Codec.BOOL.fieldOf("required").forGetter(BlueprintValueDouble::required)
    ).apply(instance, (min, max, required) -> new BlueprintValueDouble(min, max, 0d, BlueprintValue.validateForced(required))));

    public static final Codec<BlueprintValueDouble> CODEC = Codec.withAlternative(CODEC_DEFAULT, CODEC_REQUIRED);

    public BlueprintValueDouble
    {
        // TODO: do not throw runtime, it's not runtime
        if (min > max)
            throw new RuntimeException("min is bigger than max, this is not allowed (%s > %s)".formatted(min, max));
    }

    @Override
    public BlueprintValueType<Double, ?> getType()
    {
        return BlueprintValueType.DOUBLE;
    }

    @Override
    public Double convertNumeric(Number number)
    {
        return number.doubleValue();
    }

    @Override
    public ValidationResult validate(Double value)
    {
        double val = value;
        if (val < min)
            return ValidationResult.fail("Value is lower than minimum (%s < %s)", val, min);
        if (val > max)
            return ValidationResult.fail("Value is higher than maximum (%s > %s)", val, max);

        return ValidationResult.PASS;
    }
}
