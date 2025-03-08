package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import steve6472.moondust.widget.blueprint.event.SimpleCallEventBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.component.event.OnCharInput;
import steve6472.moondust.widget.component.event.OnKeyInput;

/**
 * Created by steve6472
 * Date: 2/13/2025
 * Project: MoonDust <br>
 */
public class OnKeyInputBlueprint extends SimpleCallEventBlueprint
{
    private static final OnKeyInputBlueprint INSTANCE = new OnKeyInputBlueprint();
    public static final Codec<OnKeyInputBlueprint> CODEC = createCodec(() -> INSTANCE);

    protected OnKeyInputBlueprint()
    {
        super(UIEventType.ON_KEY_INPUT, OnKeyInput.INSTANCE);
    }
}
