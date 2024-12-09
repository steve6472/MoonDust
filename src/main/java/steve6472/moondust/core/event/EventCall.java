package steve6472.moondust.core.event;

import steve6472.moondust.widget.blueprint.event.UIEvent;
import steve6472.moondust.widget.Widget;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
@FunctionalInterface
public interface EventCall<T extends UIEvent>
{
    void call(Widget component, T event);
}
