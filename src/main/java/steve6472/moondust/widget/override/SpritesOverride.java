package steve6472.moondust.widget.override;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.widget.BlueprintOverride;
import steve6472.moondust.widget.component.Sprites;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public record SpritesOverride(Map<String, Key> replace, Map<String, Key> add, Map<String, Key> remove) implements BlueprintOverride<Sprites>
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "sprites");
    public static final Codec<SpritesOverride> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.MAP_STRING_KEY.optionalFieldOf("replace", Map.of()).forGetter(SpritesOverride::replace),
        ExtraCodecs.MAP_STRING_KEY.optionalFieldOf("add", Map.of()).forGetter(SpritesOverride::add),
        ExtraCodecs.MAP_STRING_KEY.optionalFieldOf("remove", Map.of()).forGetter(SpritesOverride::remove)
    ).apply(instance, SpritesOverride::new));

    @Override
    public Sprites override(Sprites source)
    {
        Map<String, Key> sprites = new HashMap<>(source.sprites());
        sprites.putAll(add);
        replace.forEach(sprites::replace);
        remove.forEach(sprites::remove);

        return new Sprites(sprites);
    }

    @Override
    public Class<Sprites> target()
    {
        return Sprites.class;
    }
}
