package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.core.Mergeable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record Scripts(Map<String, Key> scripts) implements Mergeable<Scripts>
{
    public static final Codec<Scripts> CODEC = Codec.unboundedMap(Codec.STRING, Key.CODEC).xmap(Scripts::new, Scripts::scripts);

    @Override
    public Scripts merge(Scripts left, Scripts right)
    {
        Map<String, Key> map = new HashMap<>(left.scripts);
        map.putAll(right.scripts);
        return new Scripts(map);
    }

    public Key get(String id)
    {
        return scripts.get(id);
    }
}
