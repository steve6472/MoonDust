package steve6472.moondust.widget.component.event;

/**
 * Created by steve6472
 * Date: 2/13/2025
 * Project: MoonDust <br>
 */
public record OnCharInput(int codepoint) implements UIEvent
{
    public static final OnCharInput INSTANCE = new OnCharInput(-42);
}
