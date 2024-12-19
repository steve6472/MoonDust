package steve6472.moondust.widget.component.event;

/**
 * Created by steve6472
 * Date: 12/18/2024
 * Project: MoonDust <br>
 */
public class OnRender implements UIEvent
{
    public static final OnRender INSTANCE = new OnRender();

    private OnRender()
    {

    }

    @Override
    public String toString()
    {
        return "OnRender{}";
    }
}
