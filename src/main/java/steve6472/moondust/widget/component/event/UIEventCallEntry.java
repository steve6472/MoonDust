package steve6472.moondust.widget.component.event;

import steve6472.core.registry.Key;
import steve6472.moondust.widget.blueprint.event.condition.EventCondition;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
public record UIEventCallEntry(Key call, Object event, EventCondition condition)
{
}
