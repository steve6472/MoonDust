package steve6472.moondust.widget.component.event;

import org.jetbrains.annotations.ApiStatus;
import steve6472.moondust.widget.Widget;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
@FunctionalInterface
@ApiStatus.Obsolete
public interface UIEventCall<T extends UIEvent>
{
    void call(Widget widget, T event);
}
