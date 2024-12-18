package steve6472.moondust.widget.component.event;

/**
 * Created by steve6472
 * Date: 12/18/2024
 * Project: MoonDust <br>
 */
public class OnInit implements UIEvent
{
    public static final OnInit INSTANCE = new OnInit();

    private OnInit()
    {

    }

    @Override
    public String toString()
    {
        return "OnInit{}";
    }
}
