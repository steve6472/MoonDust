package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record FocusedSprite(Key sprite)
{
    public static final Codec<FocusedSprite> CODEC = Key.CODEC.xmap(FocusedSprite::new, FocusedSprite::sprite);
}
