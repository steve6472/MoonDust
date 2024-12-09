package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import org.joml.Vector2i;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.ClickboxOffset;
import steve6472.moondust.widget.component.SpriteOffset;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
public record ClickboxOffsetBlueprint(Vector2i offset) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "clickbox_offset");
    public static final Codec<ClickboxOffsetBlueprint> CODEC = ExtraCodecs.VEC_2I.xmap(ClickboxOffsetBlueprint::new, ClickboxOffsetBlueprint::offset);

    @Override
    public List<?> createComponents()
    {
        return List.of(new ClickboxOffset(offset.x, offset.y));
    }
}
