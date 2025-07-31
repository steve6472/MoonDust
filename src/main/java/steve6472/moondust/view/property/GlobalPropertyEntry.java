package steve6472.moondust.view.property;

import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;

/**
 * Created by steve6472
 * Date: 7/25/2025
 * Project: MoonDust <br>
 */
public record GlobalPropertyEntry(Key key, Property<?> property) implements Keyable
{
}
