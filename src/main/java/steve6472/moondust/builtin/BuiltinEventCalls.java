package steve6472.moondust.builtin;

import org.jetbrains.annotations.Nullable;
import steve6472.core.registry.Key;
import steve6472.flare.registry.FlareRegistries;
import steve6472.flare.ui.font.render.UITextLine;
import steve6472.moondust.*;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.blueprint.event.condition.Tristate;
import steve6472.moondust.widget.component.CurrentSprite;
import steve6472.moondust.widget.component.CustomData;
import steve6472.moondust.widget.component.MDTextLine;
import steve6472.moondust.widget.component.event.*;

import java.util.Optional;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class BuiltinEventCalls
{
    public static void init()
    {
        /*
         * Icons
         */
        create(key("icon/hover/on"),  (Widget widget, OnMouseEnter _) -> setCurrentSprite(widget, widget.isEnabled() && !widget.internalStates().hovered ? "hovered" : null));
        create(key("icon/hover/off"), (Widget widget, OnMouseLeave _) -> setCurrentSprite(widget, widget.isEnabled() && widget.internalStates().hovered ? "normal" : null));
        create(key("icon/press"),     (Widget widget, OnMousePress _) -> setCurrentSprite(widget, widget.isEnabled() ? "pressed" : null));
        create(key("icon/release"), (Widget widget, OnMouseRelease _) -> setCurrentSprite(widget, widget.isEnabled() ? (widget.internalStates().hovered ? "hovered" : "normal") : null));
        create(key("icon/change_enabled"), (Widget widget, OnEnableStateChange _) -> setCurrentSprite(widget, widget.isEnabled() ? (widget.internalStates().hovered ? "hovered" : "normal") : "disabled"));

        /*
         * Label
         */
        create(key("label/hover/on"),  (Widget widget, OnMouseEnter _) -> replaceStyle(widget, pickStyle(widget)));
        create(key("label/hover/off"), (Widget widget, OnMouseLeave _) -> replaceStyle(widget, pickStyle(widget)));
        create(key("label/change_enabled"), (Widget widget, OnEnableStateChange _) -> replaceStyle(widget, pickStyle(widget)));

        /*
         * Specific
         */
        // Button
        create(key("init/button"), (Widget widget, OnInit _) -> {
            widget.getChild("label").ifPresent(child -> {
                Optional<MDTextLine> component = child.getComponent(MDTextLine.class);
                component.ifPresent(mdLine -> {
                    UITextLine line = mdLine.line();
                    CustomData customData = widget.customData();
                    String label = customData.getString(Keys.GENERIC_LABEL);
                    if (label == null)
                    {
                        widget.removeChild("label");
                        return;
                    }
                    child.addComponent(new MDTextLine(new UITextLine(label, line.size(), line.style(), line.anchor()), mdLine.offset()));
                });
            });
            replaceStyle(widget, pickStyle(widget, widget.internalStates().hovered || widget.internalStates().directHover ? Tristate.TRUE : Tristate.FALSE));
        });
    }

    private static <T extends UIEvent> void create(Key key, UIEventCall<T> eventCall)
    {
        MoonDustRegistries.EVENT_CALLS.put(key, eventCall);
    }

    private static Key key(String id)
    {
        return Key.withNamespace(MoonDustConstants.NAMESPACE, id);
    }

    /*
     * Utils
     */

    public interface Keys
    {
        /*
         * Generic
         */

        // Data
        Key GENERIC_LABEL = Key.withNamespace(MoonDustConstants.NAMESPACE, "generic/label");
        Key GENERIC_LABEL_SHADOW = Key.withNamespace(MoonDustConstants.NAMESPACE, "generic/label_shadow");

        /*
         * Button
         */

        // Fonts
        Key BUTTON_SHADOW_HOVER = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/shadow/hover");
        Key BUTTON_SHADOW_NORMAL = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/shadow/normal");
        Key BUTTON_SHADOW_DISABLED = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/shadow/disabled");
        Key BUTTON_HOVER = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/hover");
        Key BUTTON_NORMAL = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/normal");
        Key BUTTON_DISABLED = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/disabled");
    }



    private static Key pickStyle(Widget widget)
    {
        return pickStyle(widget, Tristate.IGNORE);
    }

    private static Key pickStyle(Widget widget, Tristate hoverOverride)
    {
        boolean hover = hoverOverride == Tristate.IGNORE ? !widget.internalStates().hovered : (hoverOverride == Tristate.TRUE);
        boolean shadow = widget.customData().getFlag(Keys.GENERIC_LABEL_SHADOW);

        if (widget.isEnabled())
        {
            if (shadow)
                return hover ? Keys.BUTTON_SHADOW_HOVER : Keys.BUTTON_SHADOW_NORMAL;
            else
                return hover ? Keys.BUTTON_HOVER : Keys.BUTTON_NORMAL;
        } else
        {
            return shadow ? Keys.BUTTON_SHADOW_DISABLED : Keys.BUTTON_DISABLED;
        }
    }

    private static void replaceStyle(Widget widget, Key styleKey)
    {
        widget.getChild("label").ifPresent(child -> {
            Optional<MDTextLine> component = child.getComponent(MDTextLine.class);
            component.ifPresent(mdLine -> {
                UITextLine line = mdLine.line();
                child.addComponent(new MDTextLine(new UITextLine(line.text(), line.size(), FlareRegistries.FONT_STYLE.get(styleKey), line.anchor()), mdLine.offset()));
            });
        });
    }

    private static void setCurrentSprite(Widget widget, @Nullable String sprite)
    {
        if (sprite == null)
            return;

        widget.getComponent(CurrentSprite.class).ifPresent(currentSprite -> currentSprite.setSprite(sprite));
    }
}
