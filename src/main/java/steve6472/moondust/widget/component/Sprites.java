package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;

import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record Sprites(Map<String, Key> sprites)
{
    public static final Codec<Sprites> CODEC = ExtraCodecs.MAP_STRING_KEY.xmap(Sprites::new, Sprites::sprites);
}
