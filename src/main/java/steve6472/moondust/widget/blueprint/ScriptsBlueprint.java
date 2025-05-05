package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.Scripts;
import steve6472.moondust.widget.component.Sprites;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record ScriptsBlueprint(Map<String, Key> sprites) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "scripts");
    public static final Codec<ScriptsBlueprint> CODEC = ExtraCodecs.mapListCodec(Codec.STRING, Key.CODEC).xmap(ScriptsBlueprint::new, ScriptsBlueprint::sprites);

    @Override
    public List<?> createComponents()
    {
        return List.of(new Scripts(new HashMap<>(sprites)));
    }
}
