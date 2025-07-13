package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 */
public class Bounds implements IBounds
{
    public static final Codec<Bounds> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("width").forGetter(o -> o.width),
        Codec.INT.fieldOf("height").forGetter(o -> o.height)
    ).apply(instance, Bounds::new));

    public int width, height;

    public Bounds(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString()
    {
        return "Bounds{" + "width=" + width + ", height=" + height + '}';
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
