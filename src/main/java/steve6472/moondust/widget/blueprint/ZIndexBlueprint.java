package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.ZIndex;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/19/2024
 * Project: MoonDust <br>
 */
public record ZIndexBlueprint(float index) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "z_index");

    // TODO: verify only negative or 0
    public static final Codec<ZIndexBlueprint> CODEC = Codec.FLOAT.xmap(ZIndexBlueprint::new, ZIndexBlueprint::index);

    @Override
    public List<?> createComponents()
    {
        return List.of(new ZIndex(index));
    }
}
