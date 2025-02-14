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
public record Sprites(Map<String, Key> sprites) implements Mergeable<Sprites>
{
    @Override
    public Sprites merge(Sprites left, Sprites right)
    {
        Map<String, Key> map = new HashMap<>(left.sprites);
        map.putAll(right.sprites);
        return new Sprites(map);
    }

    public Key get(String id)
    {
        return sprites.get(id);
    }
}
