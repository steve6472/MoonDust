package steve6472.moondust.builtin;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
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
        create(key("icon/hover/on"),  (Widget widget, OnMouseEnter _) -> setCurrentSprite(widget, widget.isEnabled() && !widget.internalStates().hovered ? ID.SPRITE_HOVER : null));
        create(key("icon/hover/off"), (Widget widget, OnMouseLeave _) -> setCurrentSprite(widget, widget.isEnabled() && widget.internalStates().hovered ? ID.SPRITE_NORMAL : null));
        create(key("icon/press"),     (Widget widget, OnMousePress _) -> setCurrentSprite(widget, widget.isEnabled() ? ID.SPRITE_PRESSED : null));
        create(key("icon/release"), (Widget widget, OnMouseRelease _) -> setCurrentSprite(widget, widget.isEnabled() ? (widget.internalStates().hovered ? ID.SPRITE_HOVER : ID.SPRITE_NORMAL) : null));
        create(key("icon/change_enabled"), (Widget widget, OnEnableStateChange _) -> setCurrentSprite(widget, widget.isEnabled() ? (widget.internalStates().hovered ? ID.SPRITE_HOVER : ID.SPRITE_NORMAL) : ID.SPRITE_DISABLED));
        create(key("icon/change_enabled_no_hover"), (Widget widget, OnEnableStateChange _) -> setCurrentSprite(widget, widget.isEnabled() ? ID.SPRITE_NORMAL : ID.SPRITE_DISABLED));

        /*
         * Label
         */
        create(key("label/hover/on"),  (Widget widget, OnMouseEnter _) -> replaceStyle(widget, pickStyle(widget)));
        create(key("label/hover/off"), (Widget widget, OnMouseLeave _) -> replaceStyle(widget, pickStyle(widget)));
        create(key("label/change_enabled"), (Widget widget, OnEnableStateChange _) -> replaceStyle(widget, pickStyle(widget)));
        create(key("label/change_enabled_no_hover"), (Widget widget, OnEnableStateChange _) -> replaceStyle(widget, pickStyle(widget, Tristate.FALSE)));
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

        /*
         * Spinner
         */

        Consumer<Widget> updateLabel = widget -> {
            widget.getChild("label").ifPresent(childLabel -> {
                childLabel.getComponent(MDTextLine.class).ifPresent(label -> {
                    CustomData data = widget.customData();
                    String unformattedLabel = data.getString(Keys.GENERIC_LABEL);
                    if (unformattedLabel == null) return;

                    String valueNumberFormat = Optional.ofNullable(data.getString(Keys.SPINNER_NUMBER_FORMAT_VALUE)).orElse(DEFAULT_NUMBER_FORMAT);
                    String minFormat = Optional.ofNullable(data.getString(Keys.SPINNER_NUMBER_FORMAT_MIN)).orElse(DEFAULT_NUMBER_FORMAT);
                    String maxFormat = Optional.ofNullable(data.getString(Keys.SPINNER_NUMBER_FORMAT_MAX)).orElse(DEFAULT_NUMBER_FORMAT);
                    String incrementFormat = Optional.ofNullable(data.getString(Keys.SPINNER_NUMBER_FORMAT_INCREMENT)).orElse(DEFAULT_NUMBER_FORMAT);

                    unformattedLabel = unformattedLabel.replace("%value%", valueNumberFormat.formatted(data.getFloat(Keys.SPINNER_VALUE)));
                    unformattedLabel = unformattedLabel.replace("%min%", minFormat.formatted(data.getFloat(Keys.SPINNER_MIN)));
                    unformattedLabel = unformattedLabel.replace("%max%", maxFormat.formatted(data.getFloat(Keys.SPINNER_MAX)));
                    unformattedLabel = unformattedLabel.replace("%increment%", incrementFormat.formatted(data.getFloat(Keys.SPINNER_INCREMENT)));

                    childLabel.replaceComponent(label.replaceText(unformattedLabel));
                });
            });
        };

        create(key("spinner/update_label"), (Widget widget, OnDataChange _) -> updateLabel.accept(widget));
        create(key("spinner/init"), (Widget widget, OnInit _) ->
        {
            updateLabel.accept(widget);
            replaceStyle(widget, pickStyle(widget, Tristate.FALSE));
        });

        create(key("spinner/increment"), (Widget widget, OnMouseRelease _) -> {
            widget.parent().ifPresent(parent -> {
                CustomData data = parent.customData();
                float increment = data.getFloat(Keys.SPINNER_INCREMENT);
                float value = data.getFloat(Keys.SPINNER_VALUE);

                // Calls spinner/verify_bounds
                data.putFloat(Keys.SPINNER_VALUE, value + increment);
            });
        });

        create(key("spinner/decrement"), (Widget widget, OnMouseRelease _) -> {
            widget.parent().ifPresent(parent -> {
                CustomData data = parent.customData();
                float increment = data.getFloat(Keys.SPINNER_INCREMENT);
                float value = data.getFloat(Keys.SPINNER_VALUE);

                // Calls spinner/verify_bounds
                data.putFloat(Keys.SPINNER_VALUE, value - increment);
            });
        });

        create(key("spinner/verify_bounds"), (Widget widget, OnDataChange _) -> {
            CustomData data = widget.customData();
            float value = data.getFloat(Keys.SPINNER_VALUE);
            float min = data.getFloat(Keys.SPINNER_MIN);
            float max = data.getFloat(Keys.SPINNER_MAX);

            // invalid state, do nothing, prevent recursion
            if (max <= min)
                return;

            if (value > max)
                data.putFloat(Keys.SPINNER_VALUE, max);
            if (value < min)
                data.putFloat(Keys.SPINNER_VALUE, min);
        });

        create(key("spinner/change_enabled"), (Widget widget, OnEnableStateChange _) -> {
            widget.getChild("up").ifPresent(child -> child.setEnabled(widget.isEnabled()));
            widget.getChild("down").ifPresent(child -> child.setEnabled(widget.isEnabled()));
        });

        /*
         * Text field
         */
        create(key("text_field/char_input"), (Widget widget, OnCharInput charInput) -> {
            widget.getChild("label").ifPresent(child -> {
                child.getComponent(MDTextLine.class).ifPresent(label -> {
                    String text = label.line().text();
                    text = text + Character.toString(charInput.codepoint());
                    child.replaceComponent(label.replaceText(text));
                });
            });
        });

        create(key("text_field/key_input"), (Widget widget, OnKeyInput keyInput) -> {
            widget.getChild("label").ifPresent(child -> {
                child.getComponent(MDTextLine.class).ifPresent(label -> {
                    String text = label.line().text();

                    if (keyInput.action() == GLFW.GLFW_RELEASE)
                        return;

                    if (keyInput.key() == GLFW.GLFW_KEY_BACKSPACE)
                    {
                        if (!text.isEmpty())
                            text = text.substring(0, text.length() - 1);
                    }

                    child.replaceComponent(label.replaceText(text));
                });
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
