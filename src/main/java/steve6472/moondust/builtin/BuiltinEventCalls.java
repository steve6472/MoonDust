package steve6472.moondust.builtin;

import org.jetbrains.annotations.Nullable;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.flare.registry.FlareRegistries;
import steve6472.flare.ui.font.render.UITextLine;
import steve6472.moondust.*;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.blueprint.event.condition.Tristate;
import steve6472.moondust.widget.component.CurrentSprite;
import steve6472.moondust.widget.component.CustomData;
import steve6472.moondust.widget.component.MDTextLine;
import steve6472.moondust.widget.component.Styles;
import steve6472.moondust.widget.component.event.*;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class BuiltinEventCalls
{
    private static final Logger LOGGER = Log.getLogger(BuiltinEventCalls.class);

    public static void init()
    {
        /*
         * Icons
         */
        create(key("icon/hover/on"),  (Widget widget, OnMouseEnter _) -> setCurrentSprite(widget, widget.isEnabled() && !widget.internalStates().hovered ? "hover" : null));
        create(key("icon/hover/off"), (Widget widget, OnMouseLeave _) -> setCurrentSprite(widget, widget.isEnabled() && widget.internalStates().hovered ? "normal" : null));
        create(key("icon/press"),     (Widget widget, OnMousePress _) -> setCurrentSprite(widget, widget.isEnabled() ? "pressed" : null));
        create(key("icon/release"), (Widget widget, OnMouseRelease _) -> setCurrentSprite(widget, widget.isEnabled() ? (widget.internalStates().hovered ? "hover" : "normal") : null));
        create(key("icon/change_enabled"), (Widget widget, OnEnableStateChange _) -> setCurrentSprite(widget, widget.isEnabled() ? (widget.internalStates().hovered ? "hover" : "normal") : "disabled"));

        /*
         * Label
         */
        create(key("label/hover/on"),  (Widget widget, OnMouseEnter _) -> replaceStyle(widget, pickStyle(widget)));
        create(key("label/hover/off"), (Widget widget, OnMouseLeave _) -> replaceStyle(widget, pickStyle(widget)));
        create(key("label/change_enabled"), (Widget widget, OnEnableStateChange _) -> replaceStyle(widget, pickStyle(widget)));
        create(key("label/init"), (Widget widget, OnInit _) -> {
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

    public interface ID
    {
        String STYLE_NORMAL = "normal";
        String STYLE_DISABLED = "disabled";
        String STYLE_HOVER = "hover";
    }

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

        // Default styles
        Key BUTTON_SHADOW_NORMAL = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/shadow/normal");
        Key BUTTON_SHADOW_DISABLED = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/shadow/disabled");
        Key BUTTON_SHADOW_HOVER = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/shadow/hover");
        Key BUTTON_NORMAL = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/normal");
        Key BUTTON_DISABLED = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/disabled");
        Key BUTTON_HOVER = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/hover");
    }



    private static Key pickStyle(Widget widget)
    {
        return pickStyle(widget, Tristate.IGNORE);
    }

    private static Key pickStyle(Widget widget, Tristate hoverOverride)
    {
        boolean hover = hoverOverride == Tristate.IGNORE ? !widget.internalStates().hovered : (hoverOverride == Tristate.TRUE);

        Optional<Styles> stylesOpt = widget.getComponent(Styles.class);

        if (stylesOpt.isEmpty())
            return null;

        Styles styles = stylesOpt.get();

        if (widget.isEnabled())
        {
            return hover ? styles.get(ID.STYLE_HOVER) : styles.get(ID.STYLE_NORMAL);
        } else
        {
            return styles.get(ID.STYLE_DISABLED);
        }
    }

    private static void replaceStyle(Widget widget, @Nullable Key styleKey)
    {
        if (styleKey == null)
            return;

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
