package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.flare.registry.FlareRegistries;
import steve6472.flare.ui.font.render.UITextLine;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.CurrentSprite;
import steve6472.moondust.widget.component.CustomData;
import steve6472.moondust.widget.component.MDTextLine;
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
        interface Style
        {
            interface Shadow
            {
                Key HOVER = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/shadow/hover");
                Key NORMAL = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/shadow/normal");
            }

            Key HOVER = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/hover");
            Key NORMAL = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/normal");
        }

        boolean SHADOW = false;

        static void replaceStyle(Widget widget, Key styleKey)
        {
            widget.getChild("label").ifPresent(child -> {
                child.getComponent(MDTextLine.class).ifPresent(mdLine -> {
                    UITextLine line = mdLine.line();
                    child.addComponent(new MDTextLine(new UITextLine(line.text(), line.size(), FlareRegistries.FONT_STYLE.get(styleKey), line.anchor()), mdLine.offset()));
                });
            });
        }

        static Key pickStyle(boolean hover)
        {
            if (SHADOW)
                return hover ? Style.Shadow.HOVER : Style.Shadow.NORMAL;
            else
                return hover ? Style.HOVER : Style.NORMAL;
        }

        UIEventCall<OnMouseEnter> MOUSE_ENTER = create(key("button/hover_on"), (widget, event) -> {
            widget.getComponent(CurrentSprite.class).ifPresent(currentSprite -> {
                if (currentSprite.sprite.equals("normal"))
                {
                    currentSprite.setSprite("hovered");
                    replaceStyle(widget, pickStyle(true));
                }
            });
        });
        UIEventCall<OnMouseLeave> MOUSE_LEAVE = create(key("button/hover_off"), (widget, event) -> {
            widget.getComponent(CurrentSprite.class).ifPresent(currentSprite -> {
                if (currentSprite.sprite.equals("hovered"))
                {
                    currentSprite.setSprite("normal");
                    replaceStyle(widget, pickStyle(false));
                }
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
                {
                    currentSprite.setSprite("hovered");
                    replaceStyle(widget, pickStyle(true));
                } else
                {
                    currentSprite.setSprite("normal");
                    replaceStyle(widget, pickStyle(false));
                }
            });
        });

        UIEventCall<OnInit> INIT_LABEL = create(key("button/init_label"), (widget, event) -> {
            widget.getChild("label").ifPresent(child -> {
                child.getComponent(MDTextLine.class).ifPresent(mdLine -> {
                    UITextLine line = mdLine.line();
                    CustomData customData = widget.customData();
                    String label = customData.getString(Key.withNamespace("button", "label"));
                    if (label == null) return;
                    child.addComponent(new MDTextLine(new UITextLine(label, line.size(), line.style(), line.anchor()), mdLine.offset()));
                });
            });
            if (widget.internalStates().hovered || widget.internalStates().directHover)
                replaceStyle(widget, pickStyle(true));
            else
                replaceStyle(widget, pickStyle(false));
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
