package steve6472.moondust.widget.component;

import java.util.Objects;

/**
 * Created by steve6472
 * Date: 8/1/2025
 * Project: MoonDust <br>
 */
public final class Size
{
    private int width;
    private int height;

    /**
     *
     */
    public Size(int width, int height)
    {
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
    }

    public int width()
    {
        return width;
    }

    public int height()
    {
        return height;
    }

    public void set(int width, int height)
    {
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (Size) obj;
        return this.width == that.width && this.height == that.height;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(width, height);
    }

    @Override
    public String toString()
    {
        return "Size[" + "width=" + width + ", " + "height=" + height + ']';
    }

}
