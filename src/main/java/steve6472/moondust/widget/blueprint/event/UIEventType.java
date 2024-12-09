package steve6472.moondust.widget.blueprint.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Type;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.widget.blueprint.event.impl.OnMouseEnterBlueprint;
import steve6472.moondust.widget.blueprint.event.impl.OnMouseLeaveBlueprint;
import steve6472.moondust.widget.blueprint.event.impl.RandomTickBlueprint;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public final class UIEventType<T extends UIEventBlueprint> extends Type<T>
{
    public static final UIEventType<RandomTickBlueprint> RANDOM_TICK = register("random_tick", RandomTickBlueprint.CODEC);
    public static final UIEventType<OnMouseEnterBlueprint> ON_MOUSE_ENTER = register("on_mouse_enter", OnMouseEnterBlueprint.CODEC);
    public static final UIEventType<OnMouseLeaveBlueprint> ON_MOUSE_LEAVE = register("on_mouse_leave", OnMouseLeaveBlueprint.CODEC);

    public UIEventType(Key key, MapCodec<T> codec)
    {
        super(key, codec);
    }

    private static <T extends UIEventBlueprint> UIEventType<T> register(String id, Codec<T> codec)
    {
        var obj = new UIEventType<>(Key.defaultNamespace(id), MapCodec.assumeMapUnsafe(codec));
        MoonDustRegistries.EVENT_TYPE.register(obj);
        return obj;
    }
}
