package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.moondust.widget.component.CurrentSprite;
import steve6472.moondust.widget.component.event.*;

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
                if (currentSprite.sprite.equals("normal"))
                    currentSprite.setSprite("hovered");
            });
        });
        UIEventCall<OnMouseLeave> MOUSE_LEAVE = create(key("button/hover_off"), (widget, event) -> {
            widget.getComponent(CurrentSprite.class).ifPresent(currentSprite -> {
                if (currentSprite.sprite.equals("hovered"))
                    currentSprite.setSprite("normal");
            });
        });
        UIEventCall<OnMousePress> MOUSE_PRESS = create(key("button/press"), (widget, event) -> {
            widget.getComponent(CurrentSprite.class).ifPresent(currentSprite -> {
                currentSprite.setSprite("pressed");
            });
        });
        UIEventCall<OnMouseRelease> MOUSE_RELEASE = create(key("button/release"), (widget, event) -> {
            widget.getComponent(CurrentSprite.class).ifPresent(currentSprite -> {
                if (widget.internalStates().hovered)
                    currentSprite.setSprite("hovered");
                else
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
        init(Button.MOUSE_ENTER);
    }

    private static void init(Object ignored)
    {}
}
