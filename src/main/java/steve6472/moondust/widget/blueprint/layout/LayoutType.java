package steve6472.moondust.widget.blueprint.layout;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Type;
import steve6472.moondust.MoonDustRegistries;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public final class LayoutType<T extends LayoutBlueprint> extends Type<T>
{
    public static final LayoutType<AbsoluteLayoutBlueprint> ABSOLUTE = register("absolute", AbsoluteLayoutBlueprint.CODEC);

    public LayoutType(Key key, MapCodec<T> codec)
    {
        super(key, codec);
    }

    private static <T extends LayoutBlueprint> LayoutType<T> register(String id, Codec<T> codec)
    {
        var obj = new LayoutType<>(Key.defaultNamespace(id), MapCodec.assumeMapUnsafe(codec));
        MoonDustRegistries.LAYOUT_TYPE.register(obj);
        return obj;
    }
}
