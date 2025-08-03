package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;

import java.util.List;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 8/3/2025
 * Project: MoonDust <br>
 */
public record PropertiesBlueprint(Map<String, String> properties) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "properties");
    public static final Codec<PropertiesBlueprint> CODEC = Codec.unboundedMap(Codec.STRING, Codec.STRING).xmap(PropertiesBlueprint::new, PropertiesBlueprint::properties);

    @Override
    public List<?> createComponents()
    {
        // TODO: finish this
//        return List.of(new Properties(properties));
        return List.of();
    }
}
