package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.Sprites;
import steve6472.moondust.widget.component.Styles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record StylesBlueprint(Map<String, Key> styles) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "styles");
    public static final Codec<StylesBlueprint> CODEC = ExtraCodecs.mapListCodec(Codec.STRING, Key.CODEC).xmap(StylesBlueprint::new, StylesBlueprint::styles);

    @Override
    public List<?> createComponents()
    {
        return List.of(new Styles(new HashMap<>(styles)));
    }
}
