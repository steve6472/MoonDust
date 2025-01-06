package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.widget.blueprint.event.UIEventBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.component.event.OnRandomTick;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record OnRandomTickBlueprint(double probability) implements UIEventBlueprint
{
    public static final Codec<OnRandomTickBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.DOUBLE.fieldOf("probability").forGetter(OnRandomTickBlueprint::probability)
    ).apply(instance, OnRandomTickBlueprint::new));

    @Override
    public UIEventType<?> getType()
    {
        return UIEventType.ON_RANDOM_TICK;
    }

    @Override
    public List<?> createComponents()
    {
        return List.of(new OnRandomTick(probability));
    }
}
