package steve6472.moondust.core.event.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
public record States(Tristate enabled, Tristate visible)
{
    public static final Codec<States> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Tristate.CODEC.optionalFieldOf("enabled", Tristate.IGNORE).forGetter(States::enabled),
        Tristate.CODEC.optionalFieldOf("visible", Tristate.IGNORE).forGetter(States::visible)
    ).apply(instance, States::new));
}
