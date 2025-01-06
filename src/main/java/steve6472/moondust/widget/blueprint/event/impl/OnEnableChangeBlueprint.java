package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.widget.blueprint.event.UIEventBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.blueprint.event.condition.Tristate;
import steve6472.moondust.widget.component.event.OnEnableStateChange;

import java.util.List;

/**
 * Created by steve6472
 * Date: 1/7/2025
 * Project: MoonDust <br>
 */
public record OnEnableChangeBlueprint(Tristate enabled) implements UIEventBlueprint
{
    public static final Codec<OnEnableChangeBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Tristate.CODEC.optionalFieldOf("enabled", Tristate.IGNORE).forGetter(OnEnableChangeBlueprint::enabled)
    ).apply(instance, OnEnableChangeBlueprint::new));

    @Override
    public UIEventType<?> getType()
    {
        return UIEventType.ON_ENABLE_CHANGE;
    }

    @Override
    public List<?> createComponents()
    {
        return List.of(new OnEnableStateChange(enabled));
    }
}
