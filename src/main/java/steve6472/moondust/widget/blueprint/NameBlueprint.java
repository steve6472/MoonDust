package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.Name;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public record NameBlueprint(String value) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "name");
    public static final Codec<NameBlueprint> CODEC = Codec.STRING.xmap(NameBlueprint::new, NameBlueprint::value);

    @Override
    public List<?> createComponents()
    {
        return List.of(new Name(value));
    }
}
