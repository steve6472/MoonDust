package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.moondust.builtin.BuiltinEventCalls;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.Name;
import steve6472.moondust.widget.component.event.*;
import steve6472.test.DebugUILines;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class TestEventCalls
{
    public static void init()
    {
        create(key("button/exit_app"), (Widget _, OnMouseRelease _) -> MoonDustTest.instance.window().closeWindow());

        create(key("button/random_toggle_enabled"), (Widget widget, OnRandomTick _) ->
        {
            widget.setEnabled(!widget.isEnabled());
            System.out.println("Toggled to: " + widget.isEnabled());
        });

        create(key("button/test_press"), (Widget widget, OnMouseRelease _) -> widget.getComponent(Name.class).ifPresentOrElse(name -> System.out.println("Button '" + name.value() + "' pressed!"), () -> System.out.println("Unnamed Button pressed!")));
    }

    private static <T extends UIEvent> UIEventCall<T> create(Key key, UIEventCall<T> eventCall)
    {
        MoonDustRegistries.EVENT_CALLS.put(key, eventCall);
        return eventCall;
    }

    private static Key key(String id)
    {
        return Key.withNamespace(MoonDustConstants.NAMESPACE, id);
    }

    private static void checkboxInit(Widget widget, boolean flag)
    {
        widget.customData().putFlag(BuiltinEventCalls.Keys.CHECKBOX_CHECKED, flag);
    }
}
