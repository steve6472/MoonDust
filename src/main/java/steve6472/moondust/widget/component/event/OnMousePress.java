package steve6472.moondust.widget.component.event;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class OnMousePress implements UIEvent
{
    public static final OnMousePress INSTANCE = new OnMousePress();

    private OnMousePress()
    {

    }

    @Override
    public String toString()
    {
        return "OnMousePress{}";
    }
}
