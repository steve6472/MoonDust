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
        create(key("icon/hover/on"),  (Widget widget, OnMouseEnter _) -> setCurrentSprite(widget, widget.isEnabled() && !widget.internalStates().hovered ? ID.SPRITE_HOVER : null));
        create(key("icon/hover/off"), (Widget widget, OnMouseLeave _) -> setCurrentSprite(widget, widget.isEnabled() && widget.internalStates().hovered ? ID.SPRITE_NORMAL : null));
        create(key("icon/press"),     (Widget widget, OnMousePress _) -> setCurrentSprite(widget, widget.isEnabled() ? ID.SPRITE_PRESSED : null));
        create(key("icon/release"), (Widget widget, OnMouseRelease _) -> setCurrentSprite(widget, widget.isEnabled() ? (widget.internalStates().hovered ? ID.SPRITE_HOVER : ID.SPRITE_NORMAL) : null));
        create(key("icon/change_enabled"), (Widget widget, OnEnableStateChange _) -> setCurrentSprite(widget, widget.isEnabled() ? (widget.internalStates().hovered ? ID.SPRITE_HOVER : ID.SPRITE_NORMAL) : ID.SPRITE_DISABLED));

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
            replaceStyle(widget, pickStyle(widget, (widget.internalStates().hovered || widget.internalStates().directHover) ? Tristate.TRUE : Tristate.FALSE));
        });

        /*
         * Checkbox
         */
        create(key("checkbox/toggle"), (Widget widget, OnMouseRelease _) -> {
            if (!widget.isEnabled()) return;
            boolean checked = widget.customData().getFlag(Keys.CHECKBOX_CHECKED);
            // Calls checkbox/on_toggle
            widget.customData().putFlag(Keys.CHECKBOX_CHECKED, !checked);
        });

        create(key("checkbox/toggle_parent"), (Widget widget, OnMouseRelease _) -> {
            widget.parent().ifPresent(parent -> {
                if (!parent.isEnabled()) return;
                boolean checked = parent.customData().getFlag(Keys.CHECKBOX_CHECKED);
                // Calls checkbox/on_toggle
                parent.customData().putFlag(Keys.CHECKBOX_CHECKED, !checked);
            });
        });

        // Handles disabling as well
        create(key("checkbox/on_toggle"), (Widget widget, OnDataChange _) -> {
            boolean checked = widget.customData().getFlag(Keys.CHECKBOX_CHECKED);
            if (checked)
                setCurrentSprite(widget, widget.isEnabled() ? ID.SPRITE_CHECKED : ID.SPRITE_CHECKED_DISABLED);
            else
                setCurrentSprite(widget, widget.isEnabled() ? ID.SPRITE_UNCHECKED : ID.SPRITE_UNCHECKED_DISBLED);
        });

        // Handles disabling as well
        create(key("checkbox/init"), (Widget widget, OnInit _) -> {
            boolean checked = widget.customData().getFlag(Keys.CHECKBOX_CHECKED);
            if (checked)
                setCurrentSprite(widget, widget.isEnabled() ? ID.SPRITE_CHECKED : ID.SPRITE_CHECKED_DISABLED);
            else
                setCurrentSprite(widget, widget.isEnabled() ? ID.SPRITE_UNCHECKED : ID.SPRITE_UNCHECKED_DISBLED);
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

        String SPRITE_NORMAL = "normal";
        String SPRITE_DISABLED = "disabled";
        String SPRITE_HOVER = "hover";
        String SPRITE_PRESSED = "pressed";

        String SPRITE_UNCHECKED = "unchecked";
        String SPRITE_CHECKED = "checked";
        String SPRITE_UNCHECKED_DISBLED = "unchecked_disabled";
        String SPRITE_CHECKED_DISABLED = "checked_disabled";
    }

    public interface Keys
    {
        /*
         * Generic
         */

        // Data
        Key GENERIC_LABEL = key("generic/label");
        Key GENERIC_LABEL_SHADOW = key("generic/label_shadow");

        /*
         * Button
         */

        // Default styles
        Key BUTTON_SHADOW_NORMAL = key("button/shadow/normal");
        Key BUTTON_SHADOW_DISABLED = key("button/shadow/disabled");
        Key BUTTON_SHADOW_HOVER = key("button/shadow/hover");
        Key BUTTON_NORMAL = key("button/normal");
        Key BUTTON_DISABLED = key("button/disabled");
        Key BUTTON_HOVER = key("button/hover");

        /*
         * Checkbox
         */
        Key CHECKBOX_CHECKED = key("checkbox/checked");

        // Default styles
        Key CHECKBOX_SHADOW_NORMAL = key("button/shadow/normal");
        Key CHECKBOX_SHADOW_DISABLED = key("button/shadow/disabled");
        Key CHECKBOX_SHADOW_HOVER = key("button/shadow/hover");
        Key CHECKBOX_NORMAL = key("button/normal");
        Key CHECKBOX_DISABLED = key("button/disabled");
        Key CHECKBOX_HOVER = key("button/hover");
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
