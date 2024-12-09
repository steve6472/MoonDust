package steve6472.moondust.core.event.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
public enum Tristate
{
    TRUE,
    FALSE,
    IGNORE;

    private static final Codec<Tristate> CODEC_IGNORE = Codec.STRING
        .validate(str -> str.equals("ignore") ? DataResult.success(str) : DataResult.error(() -> "Invalid state, can only be true, false or \"ignore\""))
        .xmap(_ -> IGNORE, Enum::name);

    public static final Codec<Tristate> CODEC = Codec.withAlternative(CODEC_IGNORE, Codec.BOOL, b -> b ? TRUE : FALSE);
}
