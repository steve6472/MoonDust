package steve6472.moondust.widget.component.event;

import steve6472.moondust.widget.blueprint.event.condition.Tristate;

/**
 * Created by steve6472
 * Date: 1/7/2025
 * Project: MoonDust <br>
 */
public record OnEnableStateChange(Tristate enabled) implements UIEvent
{
}
