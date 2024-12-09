package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import steve6472.moondust.widget.blueprint.event.SimpleCallEventBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.component.event.OnMouseEnter;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class OnMouseEnterBlueprint extends SimpleCallEventBlueprint
{
    public static final Codec<OnMouseEnterBlueprint> CODEC = createCodec(OnMouseEnterBlueprint::new);

    protected OnMouseEnterBlueprint()
    {
        super(UIEventType.ON_MOUSE_ENTER, OnMouseEnter.INSTANCE);
    }
}
