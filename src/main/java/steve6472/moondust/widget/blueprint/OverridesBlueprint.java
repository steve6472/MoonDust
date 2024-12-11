package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.BlueprintOverride;
import steve6472.moondust.widget.component.Overrides;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record OverridesBlueprint(List<BlueprintOverride<?>> overrides) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "overrides");
    public static final Codec<OverridesBlueprint> CODEC = MoonDustRegistries.OVERRIDE.valueMapCodec().xmap(map -> {
        List<BlueprintOverride<?>> overrides = new ArrayList<>(map.size());
        map.forEach((key, value) -> {
            if (value instanceof BlueprintOverride<?> override)
            {
                overrides.add(override);
            } else
            {
                throw new RuntimeException("Value for key '%s' is not BlueprintOverride (%s)".formatted(key, value));
            }
        });
        return new OverridesBlueprint(overrides);
    }, _ -> {
        throw new RuntimeException("Encoding of Overrides Blueprint is not yet implemented. And it may never be (PR is welcome)");
    });

    @Override
    public List<?> createComponents()
    {
        return List.of(new Overrides(overrides));
    }
}
