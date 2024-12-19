package steve6472.moondust;

import org.lwjgl.glfw.GLFW;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.flare.registry.FlareRegistries;
import steve6472.flare.ui.font.render.UITextLine;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.*;
import steve6472.moondust.widget.component.event.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    interface RadioButton
    {
        Logger LOGGER = Log.getLogger(RadioButton.class);

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

        static String pickSprite(boolean enabled, boolean checked)
        {
            if (checked)
                return enabled ? "checked" : "checked_disabled";
            else
                return enabled ? "unchecked" : "unchecked_disabled";
        }

        UIEventCall<OnMouseRelease> MOUSE_RELEASE = create(key("radio_button/release"), (widget, event) -> {
            widget.getComponents(CurrentSprite.class, RadioGroup.class).ifPresent(result -> {
                CurrentSprite currentSprite = result.comp1();
                RadioGroup group = result.comp2();
                widget.parent().ifPresent(parent -> {
                    parent.getChildren()
                        .stream()
                        .filter(child -> child
                            .getComponent(RadioGroup.class)
                            .map(radioGroup -> radioGroup.groupKey.equals(group.groupKey))
                            .orElse(false))
                        .forEach(child -> child.getComponents(RadioGroup.class, CurrentSprite.class).ifPresent(res ->
                        {
                            res.comp1().selected = false;
                            res.comp2().sprite = pickSprite(child.isEnabled(), false);
                        }));
                });
                currentSprite.sprite = pickSprite(widget.isEnabled(), true);
            });
        });

        UIEventCall<OnInit> INIT = create(key("radio_button/init"), (widget, _) -> {
            widget.getComponents(RadioGroup.class, CurrentSprite.class).ifPresent(result -> {
                RadioGroup group = result.comp1();
                if (group.groupKey.equals("__ungrouped"))
                    LOGGER.warning("Radio Button does not have radio_group overriden in parent. Path: " + widget.getPath());

                widget.parent().ifPresent(parent -> {
                    List<Widget> collect = new ArrayList<>(parent
                        .getChildren()
                        .stream()
                        .filter(child -> child
                            .getComponent(RadioGroup.class)
                            .map(radioGroup -> radioGroup.groupKey.equals(group.groupKey))
                            .orElse(false))
                        .toList());
                    collect.add(widget);

                    boolean selectedFound = false;
                    boolean warningLogged = false;
                    for (Widget wgd : collect)
                    {
                        RadioGroup group1 = wgd.getComponent(RadioGroup.class).orElseThrow();

                        if (selectedFound && group.selected)
                        {
                            group.selected = false;
                            if (!warningLogged)
                                LOGGER.warning("Found multiple by-default selected radio widgets in group '" + group.groupKey + "'");
                            warningLogged = true;
                        }

                        if (group1.selected)
                        {
                            selectedFound = true;
                        }
                    }

                    result.comp2().setSprite(pickSprite(widget.isEnabled(), group.selected));
                });
            });

            widget.getChild("label").ifPresent(child -> {
                child.getComponent(MDTextLine.class).ifPresent(mdLine -> {
                    UITextLine line = mdLine.line();
                    CustomData customData = widget.customData();
                    String label = customData.getString(Key.withNamespace("radio_button", "label"));
                    if (label == null) return;
                    child.addComponent(new MDTextLine(new UITextLine(label, line.size(), line.style(), line.anchor()), mdLine.offset()));
                });
            });
        });
    }

    interface Mouse
    {
        private static Widget part(Widget parent, String sprite)
        {
            BlueprintFactory factory = MoonDustRegistries.WIDGET_FACTORY.get(Key.withNamespace(MoonDustConstants.NAMESPACE, "mouse_part"));
            Widget widget = Widget.withParent(factory, parent);
            widget.addComponent(new CurrentSprite(sprite));
            widget.addComponent(new Name("part_" + sprite));
            return widget;
        }

        private static void process(Widget widget, boolean state, String part)
        {
            widget.getChild("part_" + part).ifPresentOrElse(_ -> {
                if (!state)
                    widget.removeChild("part_" + part);
            }, () -> {
                if (state)
                    widget.addChild(part(widget, part));
            });
        }

        UIEventCall<OnRender> RENDER = create(key("mouse/tick"), (widget, _) -> {
            long window = MoonDustTest.instance.window().window();

            boolean leftPress = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
            boolean rightPress = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
            boolean middlePress = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_MIDDLE) == GLFW.GLFW_PRESS;
            // TODO: add scroll input to Flare

            process(widget, leftPress, "left");
            process(widget, rightPress, "right");
            process(widget, middlePress, "middle");
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
        init(RadioButton.MOUSE_RELEASE);
        init(Mouse.RENDER);
    }

    private static void init(Object ignored)
    {}
}
