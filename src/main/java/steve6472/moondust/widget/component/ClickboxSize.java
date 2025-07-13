package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
public class ClickboxSize implements IBounds
{
    public static final Codec<ClickboxSize> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("width").forGetter(o -> o.width),
        Codec.INT.fieldOf("height").forGetter(o -> o.height)
    ).apply(instance, ClickboxSize::new));

    public int width, height;

    public ClickboxSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString()
    {
        return "ClickboxSize{" + "width=" + width + ", height=" + height + '}';
    }

    @Override
    public int width()
    {
        return width;
    }

    @Override
    public int height()
    {
        return height;
    }
}
