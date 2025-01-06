package steve6472.moondust.widget.blueprint.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Type;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.widget.blueprint.event.impl.*;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public final class UIEventType<T extends UIEventBlueprint> extends Type<T>
{
    public static final UIEventType<OnRandomTickBlueprint> ON_RANDOM_TICK = register("on_random_tick", OnRandomTickBlueprint.CODEC);
    public static final UIEventType<OnMouseEnterBlueprint> ON_MOUSE_ENTER = register("on_mouse_enter", OnMouseEnterBlueprint.CODEC);
    public static final UIEventType<OnMouseLeaveBlueprint> ON_MOUSE_LEAVE = register("on_mouse_leave", OnMouseLeaveBlueprint.CODEC);
    public static final UIEventType<OnMousePressBlueprint> ON_MOUSE_PRESS = register("on_mouse_press", OnMousePressBlueprint.CODEC);
    public static final UIEventType<OnMouseReleaseBlueprint> ON_MOUSE_RELEASE = register("on_mouse_release", OnMouseReleaseBlueprint.CODEC);
    public static final UIEventType<OnInitBlueprint> ON_INIT = register("on_init", OnInitBlueprint.CODEC);
    public static final UIEventType<OnRenderBlueprint> ON_RENDER = register("on_render", OnRenderBlueprint.CODEC);
    public static final UIEventType<OnEnableChangeBlueprint> ON_ENABLE_CHANGE = register("on_enable_change", OnEnableChangeBlueprint.CODEC);

    public UIEventType(Key key, MapCodec<T> codec)
    {
        super(key, codec);
    }

    private static <T extends UIEventBlueprint> UIEventType<T> register(String id, Codec<T> codec)
    {
        var obj = new UIEventType<>(Key.withNamespace(MoonDustConstants.NAMESPACE, id), MapCodec.assumeMapUnsafe(codec));
        MoonDustRegistries.EVENT_TYPE.register(obj);
        return obj;
    }
}
