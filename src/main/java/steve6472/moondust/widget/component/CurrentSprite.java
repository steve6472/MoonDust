package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 */
public class CurrentSprite
{
    public String sprite;

    public static final Codec<CurrentSprite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("sprite").forGetter(CurrentSprite::sprite)
    ).apply(instance, CurrentSprite::new));

    public CurrentSprite(String sprite)
    {
        this.sprite = sprite;
    }

    public void setSprite(String sprite)
    {
        this.sprite = sprite;
    }

    public String sprite()
    {
        return sprite;
    }

    @Override
    public String toString()
    {
        return "CurrentSprite{" + "sprite='" + sprite + '\'' + '}';
    }
}
