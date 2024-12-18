package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import steve6472.moondust.widget.blueprint.event.SimpleCallEventBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.component.event.OnInit;

/**
 * Created by steve6472
 * Date: 12/18/2024
 * Project: MoonDust <br>
 */
public class OnInitBlueprint extends SimpleCallEventBlueprint
{
    private static final OnInitBlueprint INSTANCE = new OnInitBlueprint();
    public static final Codec<OnInitBlueprint> CODEC = createCodec(() -> INSTANCE);

    protected OnInitBlueprint()
    {
        super(UIEventType.ON_INIT, OnInit.INSTANCE);
    }
}
