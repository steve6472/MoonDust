package steve6472.moondust.widget.blueprint.event.condition;

import steve6472.moondust.widget.Widget;

import java.util.function.Predicate;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
@FunctionalInterface
public interface Condition extends Predicate<Widget>
{
}
