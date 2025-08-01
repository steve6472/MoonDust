package steve6472.moondust.widget.blueprint;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import org.joml.Vector2i;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.ClickboxSize;
import steve6472.moondust.widget.component.IBounds;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
public record ClickboxSizeBlueprint(Pair<IBounds.Val, IBounds.Val> size) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "clickbox_size");
    public static final Codec<ClickboxSizeBlueprint> CODEC = IBounds.VEC_2.xmap(ClickboxSizeBlueprint::new, ClickboxSizeBlueprint::size);

    @Override
    public List<?> createComponents()
    {
        return List.of(new ClickboxSize(size.getFirst(), size.getSecond()));
    }
}
