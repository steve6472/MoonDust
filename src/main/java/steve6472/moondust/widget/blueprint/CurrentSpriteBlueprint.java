package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.CurrentSprite;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 */
public record CurrentSpriteBlueprint(String sprite) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "current_sprite");
    public static final Codec<CurrentSpriteBlueprint> CODEC = Codec.STRING.xmap(CurrentSpriteBlueprint::new, CurrentSpriteBlueprint::sprite);

    @Override
    public List<?> createComponents()
    {
        return List.of(new CurrentSprite(sprite));
    }
}
