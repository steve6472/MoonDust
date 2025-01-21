package steve6472.moondust.widget.component.event;

import steve6472.core.registry.Key;

import java.util.List;

/**
 * Created by steve6472
 * Date: 1/7/2025
 * Project: MoonDust <br>
 */
public record OnDataChange(List<Key> floats, List<Key> ints, List<Key> strings, List<Key> flags) implements UIEvent
{
}
