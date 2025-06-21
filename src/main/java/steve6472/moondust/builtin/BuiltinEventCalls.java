package steve6472.moondust.builtin;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.flare.registry.FlareRegistries;
import steve6472.flare.ui.font.render.TextPart;
import steve6472.flare.ui.font.style.FontStyleEntry;
import steve6472.moondust.*;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.blueprint.event.condition.Tristate;
import steve6472.moondust.widget.component.*;
import steve6472.moondust.widget.component.event.*;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class BuiltinEventCalls
{
    private static final Logger LOGGER = Log.getLogger(BuiltinEventCalls.class);

    public static final String DEFAULT_NUMBER_FORMAT = "%.2f";

    public static void init()
    {
        create(key("grab_focus/release"), (Widget widget, OnMouseRelease _) -> {
            if (!widget.isFocusable())
            {
                LOGGER.warning("Tried to grab focus on unfocusable widget " + widget.getPath());
                return;
            }

            MoonDust.getInstance().focus(widget);
        });

        /*
         * Icons
         */
        create(key("icon/change_enabled"), (Widget widget, OnEnableStateChange _) -> setCurrentSprite(widget, widget.isEnabled() ? (widget.internalStates().hovered ? ID.SPRITE_HOVER : ID.SPRITE_NORMAL) : ID.SPRITE_DISABLED));

        /*
         * Text
         */
        create(key("text/hover/on"),  (Widget widget, OnMouseEnter _) -> replaceStyleText(widget, pickStyle(widget)));
        create(key("text/hover/off"), (Widget widget, OnMouseLeave _) -> replaceStyleText(widget, pickStyle(widget)));
        create(key("text/change_enabled"), (Widget widget, OnEnableStateChange _) -> replaceStyleText(widget, pickStyle(widget)));
        create(key("text/init"), (Widget widget, OnInit _) -> {
            Optional<MDText> component = widget.getComponent(MDText.class);
            component.ifPresent(mdLine -> {
                CustomData customData = widget.customData();
                String label = customData.getString(Keys.GENERIC_LABEL);
                if (label == null)
                {
                    widget.removeComponent(MDText.class);
                    return;
                }
                mdLine.replaceText(label, 0);
                widget.addComponent(new MDText(mdLine.text(), mdLine.position()));
            });
            replaceStyle(widget, pickStyle(widget, (widget.internalStates().hovered || widget.internalStates().directHover) ? Tristate.TRUE : Tristate.FALSE));
        });

        /*
         * Text field
         */
        create(key("text_field/char_input"), (Widget widget, OnCharInput charInput) -> {
            widget.getComponent(MDText.class).ifPresent(label -> {
                String text = label.text().parts().getFirst().text();
                text = text + Character.toString(charInput.codepoint());
                label.replaceText(text, 0);
            });
        });

        create(key("text_field/key_input"), (Widget widget, OnKeyInput keyInput) -> {
            widget.getComponent(MDText.class).ifPresent(label -> {
                String text = label.text().parts().getFirst().text();

                if (keyInput.action() == GLFW.GLFW_RELEASE)
                    return;

                if (keyInput.key() == GLFW.GLFW_KEY_BACKSPACE)
                {
                    if (!text.isEmpty())
                        text = text.substring(0, text.length() - 1);
                }

                label.replaceText(text, 0);
            });
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

        /*
         * Spinner
         */
        Key SPINNER_VALUE = key("spinner/value");
        Key SPINNER_MIN = key("spinner/min");
        Key SPINNER_MAX = key("spinner/max");
        Key SPINNER_INCREMENT = key("spinner/increment");

        Key SPINNER_NUMBER_FORMAT_VALUE = key("spinner/number_format/value");
        Key SPINNER_NUMBER_FORMAT_MIN = key("spinner/number_format/min");
        Key SPINNER_NUMBER_FORMAT_MAX = key("spinner/number_format/max");
        Key SPINNER_NUMBER_FORMAT_INCREMENT = key("spinner/number_format/increment");

        Key SPINNER_SHADOW_NORMAL = key("button/shadow/normal");
        Key SPINNER_SHADOW_DISABLED = key("button/shadow/disabled");
        Key SPINNER_NORMAL = key("button/normal");
        Key SPINNER_DISABLED = key("button/disabled");
    }



    private static Key pickStyle(Widget widget)
    {
        return pickStyle(widget, Tristate.IGNORE);
    }

    private static Key pickStyle(Widget widget, Tristate hoverOverride)
    {
        boolean hover;
        if (hoverOverride == Tristate.IGNORE)
            hover = !widget.internalStates().hovered;
        else
            hover = hoverOverride == Tristate.TRUE;

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
            Optional<MDText> component = child.getComponent(MDText.class);
            component.ifPresent(mdLine -> {
                TextPart textPart = mdLine.text().parts().get(0);
                FontStyleEntry styleEntry = FlareRegistries.FONT_STYLE.get(styleKey);
                mdLine.replaceText(new TextPart(textPart.text(), textPart.size(), styleEntry), 0);
            });
        });
    }

    private static void replaceStyleText(Widget widget, @Nullable Key styleKey)
    {
        if (styleKey == null)
            return;

        widget.getComponent(MDText.class).ifPresent(mdLine -> {
            TextPart textPart = mdLine.text().parts().get(0);
            FontStyleEntry styleEntry = FlareRegistries.FONT_STYLE.get(styleKey);
            mdLine.replaceText(new TextPart(textPart.text(), textPart.size(), styleEntry), 0);
        });
    }

    private static void setCurrentSprite(Widget widget, @Nullable String sprite)
    {
        if (sprite == null)
            return;

        widget.getComponent(CurrentSprite.class).ifPresent(currentSprite -> currentSprite.setSprite(sprite));
    }
}
