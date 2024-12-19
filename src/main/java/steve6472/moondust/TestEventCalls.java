package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.event.OnMouseRelease;
import steve6472.moondust.widget.component.event.UIEvent;
import steve6472.moondust.widget.component.event.UIEventCall;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class TestEventCalls
{
    public static void init()
    {
        create(key("button/exit_app"), (Widget _, OnMouseRelease _) -> {
            MoonDustTest.instance.window().closeWindow();
        });

        create(key("radio/scale_6"), (Widget _, OnMouseRelease _) -> {
            MoonDust.getInstance().setPixelScale(6f);
        });

        create(key("radio/scale_4"), (Widget _, OnMouseRelease _) -> {
            MoonDust.getInstance().setPixelScale(4f);
        });

        create(key("radio/scale_2"), (Widget _, OnMouseRelease _) -> {
            MoonDust.getInstance().setPixelScale(2f);
        });

        create(key("radio/scale_1"), (Widget _, OnMouseRelease _) -> {
            MoonDust.getInstance().setPixelScale(1f);
        });
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
}
