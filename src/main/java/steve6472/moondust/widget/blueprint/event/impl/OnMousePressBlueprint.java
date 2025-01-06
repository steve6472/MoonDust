package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import steve6472.moondust.widget.blueprint.event.SimpleCallEventBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.component.event.OnMousePress;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class OnMousePressBlueprint extends SimpleCallEventBlueprint
{
    public static final Codec<OnMousePressBlueprint> CODEC = createCodec(OnMousePressBlueprint::new);

    protected OnMousePressBlueprint()
    {
        super(UIEventType.ON_MOUSE_PRESS, OnMousePress.INSTANCE);
    }
}
