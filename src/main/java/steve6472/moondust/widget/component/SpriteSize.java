package steve6472.moondust.widget.component;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 */
public class SpriteSize
{
    public int width, height;

    public SpriteSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString()
    {
        return "SpriteSize{" + "width=" + width + ", height=" + height + '}';
    }
}
