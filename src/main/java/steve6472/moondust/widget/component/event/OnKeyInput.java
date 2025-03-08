package steve6472.moondust.widget.component.event;

/**
 * Created by steve6472
 * Date: 2/13/2025
 * Project: MoonDust <br>
 */
public record OnKeyInput(int key, int scancode, int action, int mods) implements UIEvent
{
    public static final OnKeyInput INSTANCE = new OnKeyInput(-42, -69, -420, -7);
}
