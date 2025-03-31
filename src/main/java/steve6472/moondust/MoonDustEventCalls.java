package steve6472.moondust;

import org.lwjgl.glfw.GLFW;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.moondust.builtin.BuiltinEventCalls;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.*;
import steve6472.moondust.widget.component.event.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public interface MoonDustEventCalls
{
    interface RadioButton
    {
        Logger LOGGER = Log.getLogger(RadioButton.class);

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
                child.getComponent(MDText.class).ifPresent(mdLine -> {
                    CustomData customData = widget.customData();
                    String label = customData.getString(Key.withNamespace("radio_button", "label"));
                    if (label == null)
                    {
                        widget.removeChild("label");
                        return;
                    }
                    mdLine.replaceText(label, 0);
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
        init(RadioButton.MOUSE_RELEASE);
        init(Mouse.RENDER);
    }

    private static void init(Object ignored)
    {}
}
