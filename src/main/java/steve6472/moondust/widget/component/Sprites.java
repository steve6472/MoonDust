package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;
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
    public static final Codec<Sprites> CODEC = ExtraCodecs.MAP_STRING_KEY.xmap(Sprites::new, Sprites::sprites);

    @Override
    public Sprites merge(Sprites left, Sprites right)
    {
        Map<String, Key> map = new HashMap<>(left.sprites);
        map.putAll(right.sprites);
        return new Sprites(map);
    }
}
