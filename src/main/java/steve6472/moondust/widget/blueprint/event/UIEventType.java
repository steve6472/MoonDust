package steve6472.moondust.widget.blueprint.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Type;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.widget.blueprint.event.impl.RandomTick;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public final class UIEventType<T extends UIEvent> extends Type<T>
{
    public static final UIEventType<RandomTick> RANDOM_TICK = register("random_tick", RandomTick.CODEC);

    public UIEventType(Key key, MapCodec<T> codec)
    {
        super(key, codec);
    }

    private static <T extends UIEvent> UIEventType<T> register(String id, Codec<T> codec)
    {
        var obj = new UIEventType<>(Key.defaultNamespace(id), MapCodec.assumeMapUnsafe(codec));
        MoonDustRegistries.EVENT_TYPE.register(obj);
        return obj;
    }
}
