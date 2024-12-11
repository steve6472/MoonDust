package steve6472.moondust.widget.component;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 */
public class CurrentSprite
{
    public String sprite;

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
