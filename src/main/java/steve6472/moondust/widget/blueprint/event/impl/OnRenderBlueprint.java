package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import steve6472.moondust.widget.blueprint.event.SimpleCallEventBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.component.event.OnInit;
import steve6472.moondust.widget.component.event.OnRender;

/**
 * Created by steve6472
 * Date: 12/18/2024
 * Project: MoonDust <br>
 */
public class OnRenderBlueprint extends SimpleCallEventBlueprint
{
    private static final OnRenderBlueprint INSTANCE = new OnRenderBlueprint();
    public static final Codec<OnRenderBlueprint> CODEC = createCodec(() -> INSTANCE);

    protected OnRenderBlueprint()
    {
        super(UIEventType.ON_RENDER, OnRender.INSTANCE);
    }
}
