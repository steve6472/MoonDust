package steve6472.moondust.widget.component.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Type;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public final class PositionType<T extends Position> extends Type<T>
{
    public static final PositionType<AbsolutePos> ABSOLUTE = register("absolute", AbsolutePos.CODEC);
    public static final PositionType<RelativePos> RELATIVE = register("relative", RelativePos.CODEC);
    public static final PositionType<AnchoredPos> ANCHORED = register("anchored", AnchoredPos.CODEC);

    public PositionType(Key key, MapCodec<T> codec)
    {
        super(key, codec);
    }

    private static <T extends Position> PositionType<T> register(String id, Codec<T> codec)
    {
        var obj = new PositionType<>(MoonDustConstants.key(id), MapCodec.assumeMapUnsafe(codec));
        MoonDustRegistries.POSITION_TYPE.register(obj);
        return obj;
    }
}
