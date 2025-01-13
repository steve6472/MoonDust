package steve6472.moondust.widget.component;

import steve6472.core.registry.Key;
import steve6472.moondust.core.Mergeable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record Styles(Map<String, Key> styles) implements Mergeable<Styles>
{
    @Override
    public Styles merge(Styles left, Styles right)
    {
        Map<String, Key> map = new HashMap<>(left.styles);
        map.putAll(right.styles);
        return new Styles(map);
    }

    public Key get(String id)
    {
        return styles.get(id);
    }
}
