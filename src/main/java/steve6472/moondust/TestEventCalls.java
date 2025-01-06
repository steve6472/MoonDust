package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.CurrentSprite;
import steve6472.moondust.widget.component.event.OnInit;
import steve6472.moondust.widget.component.event.OnMouseRelease;
import steve6472.moondust.widget.component.event.UIEvent;
import steve6472.moondust.widget.component.event.UIEventCall;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class TestEventCalls
{
    public static void init()
    {
        create(key("button/exit_app"), (Widget _, OnMouseRelease _) -> {
            MoonDustTest.instance.window().closeWindow();
        });

        create(key("radio/scale_6"), (Widget _, OnMouseRelease _) -> {
            MoonDust.getInstance().setPixelScale(6f);
        });

        create(key("radio/scale_4"), (Widget _, OnMouseRelease _) -> {
            MoonDust.getInstance().setPixelScale(4f);
        });

        create(key("radio/scale_2"), (Widget _, OnMouseRelease _) -> {
            MoonDust.getInstance().setPixelScale(2f);
        });

        create(key("radio/scale_1"), (Widget _, OnMouseRelease _) -> {
            MoonDust.getInstance().setPixelScale(1f);
        });



        create(key("checkbox/bounds_init"), (Widget widget, OnInit _) -> {
            widget.customData().putInt(MoonDustEventCalls.Checkbox.CHECKED, DebugUILines.bounds ? 1 : 0);
            widget.getComponent(CurrentSprite.class).ifPresent(sprite -> sprite.setSprite(MoonDustEventCalls.Checkbox.pickSprite(widget.isEnabled(), DebugUILines.bounds)));
        });
        create(key("checkbox/bounds"), (Widget widget, OnMouseRelease _) -> {
            DebugUILines.bounds = widget.customData().getInt(MoonDustEventCalls.Checkbox.CHECKED) == 1;
        });

        create(key("checkbox/sprite_size_init"), (Widget widget, OnInit _) -> {
            widget.customData().putInt(MoonDustEventCalls.Checkbox.CHECKED, DebugUILines.spriteSize ? 1 : 0);
            widget.getComponent(CurrentSprite.class).ifPresent(sprite -> sprite.setSprite(MoonDustEventCalls.Checkbox.pickSprite(widget.isEnabled(), DebugUILines.spriteSize)));
        });
        create(key("checkbox/sprite_size"), (Widget widget, OnMouseRelease _) -> {
            DebugUILines.spriteSize = widget.customData().getInt(MoonDustEventCalls.Checkbox.CHECKED) == 1;
        });

        create(key("checkbox/clickbox_init"), (Widget widget, OnInit _) -> {
            widget.customData().putInt(MoonDustEventCalls.Checkbox.CHECKED, DebugUILines.clickbox ? 1 : 0);
            widget.getComponent(CurrentSprite.class).ifPresent(sprite -> sprite.setSprite(MoonDustEventCalls.Checkbox.pickSprite(widget.isEnabled(), DebugUILines.clickbox)));
        });
        create(key("checkbox/clickbox"), (Widget widget, OnMouseRelease _) -> {
            DebugUILines.clickbox = widget.customData().getInt(MoonDustEventCalls.Checkbox.CHECKED) == 1;
        });

        create(key("checkbox/character_init"), (Widget widget, OnInit _) -> {
            widget.customData().putInt(MoonDustEventCalls.Checkbox.CHECKED, DebugUILines.character ? 1 : 0);
            widget.getComponent(CurrentSprite.class).ifPresent(sprite -> sprite.setSprite(MoonDustEventCalls.Checkbox.pickSprite(widget.isEnabled(), DebugUILines.character)));
        });
        create(key("checkbox/character"), (Widget widget, OnMouseRelease _) -> {
            DebugUILines.character = widget.customData().getInt(MoonDustEventCalls.Checkbox.CHECKED) == 1;
        });

        create(key("checkbox/text_line_init"), (Widget widget, OnInit _) -> {
            widget.customData().putInt(MoonDustEventCalls.Checkbox.CHECKED, DebugUILines.textLine ? 1 : 0);
            widget.getComponent(CurrentSprite.class).ifPresent(sprite -> sprite.setSprite(MoonDustEventCalls.Checkbox.pickSprite(widget.isEnabled(), DebugUILines.textLine)));
        });
        create(key("checkbox/text_line"), (Widget widget, OnMouseRelease _) -> {
            DebugUILines.textLine = widget.customData().getInt(MoonDustEventCalls.Checkbox.CHECKED) == 1;
        });

        create(key("checkbox/message_init"), (Widget widget, OnInit _) -> {
            widget.customData().putInt(MoonDustEventCalls.Checkbox.CHECKED, DebugUILines.message ? 1 : 0);
            widget.getComponent(CurrentSprite.class).ifPresent(sprite -> sprite.setSprite(MoonDustEventCalls.Checkbox.pickSprite(widget.isEnabled(), DebugUILines.message)));
        });
        create(key("checkbox/message"), (Widget widget, OnMouseRelease _) -> {
            DebugUILines.message = widget.customData().getInt(MoonDustEventCalls.Checkbox.CHECKED) == 1;
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
}
