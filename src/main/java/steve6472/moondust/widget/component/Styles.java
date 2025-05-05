package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
    public static final Codec<Styles> CODEC = Codec.unboundedMap(Codec.STRING, Key.CODEC).xmap(Styles::new, Styles::styles);

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
