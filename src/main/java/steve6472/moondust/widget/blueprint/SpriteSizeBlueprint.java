package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import org.joml.Vector2i;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.widget.component.SpriteSize;
import steve6472.moondust.core.blueprint.Blueprint;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 */
public record SpriteSizeBlueprint(Vector2i size) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "sprite_size");
    public static final Codec<SpriteSizeBlueprint> CODEC = ExtraCodecs.VEC_2I.xmap(SpriteSizeBlueprint::new, SpriteSizeBlueprint::size);

    @Override
    public List<?> createComponents()
    {
        return List.of(new SpriteSize(size.x, size.y));
    }
}
