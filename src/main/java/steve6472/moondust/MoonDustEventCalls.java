package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.moondust.widget.component.CurrentSprite;
import steve6472.moondust.widget.component.event.OnMouseEnter;
import steve6472.moondust.widget.component.event.OnMouseLeave;
import steve6472.moondust.widget.component.event.UIEvent;
import steve6472.moondust.widget.component.event.UIEventCall;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public interface MoonDustEventCalls
{
    interface Button
    {
        UIEventCall<OnMouseEnter> MOUSE_ENTER = create(key("button/hover_on"), (widget, event) -> {
            widget.getComponent(CurrentSprite.class).ifPresent(currentSprite -> {
                currentSprite.setSprite("hovered");
            });
        });
        UIEventCall<OnMouseLeave> MOUSE_LEAVE = create(key("button/hover_off"), (widget, event) -> {
            widget.getComponent(CurrentSprite.class).ifPresent(currentSprite -> {
                currentSprite.setSprite("normal");
            });
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

    static void init()
    {
        UIEventCall<OnMouseEnter> mouseEnter = Button.MOUSE_ENTER;
    }
}
