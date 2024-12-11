package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.FocusedSprite;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record FocusedSpriteBlueprint(Key sprite) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "focused_sprite");
    public static final Codec<FocusedSpriteBlueprint> CODEC = Key.CODEC.xmap(FocusedSpriteBlueprint::new, FocusedSpriteBlueprint::sprite);

    @Override
    public List<?> createComponents()
    {
        return List.of(new FocusedSprite(sprite));
    }
}
