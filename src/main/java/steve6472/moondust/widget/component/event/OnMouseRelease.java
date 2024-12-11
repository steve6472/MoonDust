package steve6472.moondust.widget.component.event;

import steve6472.moondust.core.event.condition.Tristate;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record OnMouseRelease(Tristate cursorInside) implements UIEvent
{
}
