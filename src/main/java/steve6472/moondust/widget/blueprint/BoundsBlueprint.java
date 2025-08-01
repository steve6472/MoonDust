package steve6472.moondust.widget.blueprint;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import org.joml.Vector2i;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.widget.component.Bounds;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.IBounds;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 */
public record BoundsBlueprint(Pair<IBounds.Val, IBounds.Val> bounds) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "bounds");
    public static final Codec<BoundsBlueprint> CODEC = IBounds.VEC_2.xmap(BoundsBlueprint::new, BoundsBlueprint::bounds);

    @Override
    public List<?> createComponents()
    {
        return List.of(new Bounds(bounds.getFirst(), bounds.getSecond()));
    }
}
