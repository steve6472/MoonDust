package steve6472.moondust.widget.component;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
public class ClickboxSize
{
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
}
