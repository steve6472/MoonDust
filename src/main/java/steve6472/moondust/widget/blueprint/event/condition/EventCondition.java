package steve6472.moondust.widget.blueprint.event.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.widget.Widget;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record EventCondition(States states) implements Condition
{
    public static final Codec<EventCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        States.CODEC.optionalFieldOf("states", States.DEFAULT).forGetter(EventCondition::states)
    ).apply(instance, EventCondition::new));

    public static final EventCondition DEFAULT = new EventCondition(States.DEFAULT);

    @Override
    public boolean test(Widget widget)
    {
        return states.test(widget);
    }
}
