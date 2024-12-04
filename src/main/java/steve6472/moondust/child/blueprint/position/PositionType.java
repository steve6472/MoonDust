package steve6472.moondust.child.blueprint.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Type;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.child.blueprint.position.impl.AbsolutePosBlueprint;
import steve6472.moondust.child.blueprint.position.impl.RelativePosBlueprint;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public final class PositionType<T extends PositionBlueprint> extends Type<T>
{
    public static final PositionType<AbsolutePosBlueprint> ABSOLUTE = register("absolute", AbsolutePosBlueprint.CODEC);
    public static final PositionType<RelativePosBlueprint> RELATIVE = register("relative", RelativePosBlueprint.CODEC);

    public PositionType(Key key, MapCodec<T> codec)
    {
        super(key, codec);
    }

    private static <T extends PositionBlueprint> PositionType<T> register(String id, Codec<T> codec)
    {
        var obj = new PositionType<>(Key.defaultNamespace(id), MapCodec.assumeMapUnsafe(codec));
        MoonDustRegistries.POSITION_TYPE.register(obj);
        return obj;
    }
}
