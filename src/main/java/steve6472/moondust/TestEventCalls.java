package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.moondust.builtin.BuiltinEventCalls;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.Name;
import steve6472.moondust.widget.component.event.*;
import steve6472.test.DebugUILines;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class TestEventCalls
{
    public static void init()
    {
        create(key("button/exit_app"), (Widget _, OnMouseRelease _) -> MoonDustTest.instance.window().closeWindow());

        create(key("radio/scale_6"), (Widget _, OnMouseRelease _) -> MoonDust.getInstance().setPixelScale(6f));
        create(key("radio/scale_4"), (Widget _, OnMouseRelease _) -> MoonDust.getInstance().setPixelScale(4f));
        create(key("radio/scale_2"), (Widget _, OnMouseRelease _) -> MoonDust.getInstance().setPixelScale(2f));
        create(key("radio/scale_1"), (Widget _, OnMouseRelease _) -> MoonDust.getInstance().setPixelScale(1f));

        create(key("button/random_toggle_enabled"), (Widget widget, OnRandomTick _) ->
        {
            widget.setEnabled(!widget.isEnabled());
            System.out.println("Toggled to: " + widget.isEnabled());
        });

        create(key("button/test_press"), (Widget widget, OnMouseRelease _) -> widget.getComponent(Name.class).ifPresentOrElse(name -> System.out.println("Button '" + name.value() + "' pressed!"), () -> System.out.println("Unnamed Button pressed!")));


        create(key("checkbox/bounds_init"), (Widget widget, OnInit _) -> checkboxInit(widget, DebugWidgetUILines.bounds));
        create(key("checkbox/bounds"), (Widget widget, OnDataChange _) -> DebugWidgetUILines.bounds = widget.customData().getFlag(BuiltinEventCalls.Keys.CHECKBOX_CHECKED));

        create(key("checkbox/sprite_size_init"), (Widget widget, OnInit _) -> checkboxInit(widget, DebugWidgetUILines.spriteSize));
        create(key("checkbox/sprite_size"), (Widget widget, OnDataChange _) -> DebugWidgetUILines.spriteSize = widget.customData().getFlag(BuiltinEventCalls.Keys.CHECKBOX_CHECKED));

        create(key("checkbox/clickbox_init"), (Widget widget, OnInit _) -> checkboxInit(widget, DebugWidgetUILines.clickbox));
        create(key("checkbox/clickbox"), (Widget widget, OnDataChange _) -> DebugWidgetUILines.clickbox = widget.customData().getFlag(BuiltinEventCalls.Keys.CHECKBOX_CHECKED));

        create(key("checkbox/character_init"), (Widget widget, OnInit _) -> checkboxInit(widget, DebugUILines.CHARACTER.get()));
        create(key("checkbox/character"), (Widget widget, OnDataChange _) -> DebugUILines.CHARACTER.setting().set(widget.customData().getFlag(BuiltinEventCalls.Keys.CHECKBOX_CHECKED)));

        create(key("checkbox/text_line_init"), (Widget widget, OnInit _) -> checkboxInit(widget, DebugUILines.SEGMENT.get()));
        create(key("checkbox/text_line"), (Widget widget, OnDataChange _) -> DebugUILines.SEGMENT.setting().set(widget.customData().getFlag(BuiltinEventCalls.Keys.CHECKBOX_CHECKED)));

        create(key("checkbox/message_init"), (Widget widget, OnInit _) -> checkboxInit(widget, DebugUILines.MESSAGE_ANCHORS.get()));
        create(key("checkbox/message"), (Widget widget, OnDataChange _) -> DebugUILines.MESSAGE_ANCHORS.setting().set(widget.customData().getFlag(BuiltinEventCalls.Keys.CHECKBOX_CHECKED)));
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

    private static void checkboxInit(Widget widget, boolean flag)
    {
        widget.customData().putFlag(BuiltinEventCalls.Keys.CHECKBOX_CHECKED, flag);
    }
}
