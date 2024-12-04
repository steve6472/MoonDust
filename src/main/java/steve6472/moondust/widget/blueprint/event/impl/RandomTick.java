package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.widget.blueprint.event.UIEvent;
import steve6472.moondust.widget.blueprint.event.UIEventType;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record RandomTick(double probability) implements UIEvent
{
    public static final Codec<RandomTick> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.DOUBLE.fieldOf("probability").forGetter(RandomTick::probability)
    ).apply(instance, RandomTick::new));

    @Override
    public UIEventType<?> getType()
    {
        return UIEventType.RANDOM_TICK;
    }
}
