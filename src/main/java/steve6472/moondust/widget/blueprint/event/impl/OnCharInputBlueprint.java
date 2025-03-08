package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import steve6472.moondust.widget.blueprint.event.SimpleCallEventBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.component.event.OnCharInput;
import steve6472.moondust.widget.component.event.OnInit;

/**
 * Created by steve6472
 * Date: 2/13/2025
 * Project: MoonDust <br>
 */
public class OnCharInputBlueprint extends SimpleCallEventBlueprint
{
    private static final OnCharInputBlueprint INSTANCE = new OnCharInputBlueprint();
    public static final Codec<OnCharInputBlueprint> CODEC = createCodec(() -> INSTANCE);

    protected OnCharInputBlueprint()
    {
        super(UIEventType.ON_CHAR_INPUT, OnCharInput.INSTANCE);
    }
}
