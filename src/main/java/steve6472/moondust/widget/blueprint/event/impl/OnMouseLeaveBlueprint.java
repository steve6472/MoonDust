package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import steve6472.moondust.widget.blueprint.event.SimpleCallEventBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.component.event.OnMouseLeave;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class OnMouseLeaveBlueprint extends SimpleCallEventBlueprint
{
    public static final Codec<OnMouseLeaveBlueprint> CODEC = createCodec(OnMouseLeaveBlueprint::new);

    protected OnMouseLeaveBlueprint()
    {
        super(UIEventType.ON_MOUSE_LEAVE, OnMouseLeave.INSTANCE);
    }
}
