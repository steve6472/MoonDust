package steve6472.moondust.view.property;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public class BooleanProperty extends Property<Boolean>
{
    public BooleanProperty()
    {
        this(false);
    }

    public BooleanProperty(boolean value)
    {
        set(value);
    }
}
