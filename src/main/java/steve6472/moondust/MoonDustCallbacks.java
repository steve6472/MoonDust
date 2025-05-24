package steve6472.moondust;

import org.joml.Vector2i;
import steve6472.core.log.Log;
import steve6472.flare.WindowCallbacks;
import steve6472.flare.input.UserInput;
import steve6472.moondust.widget.Panel;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.event.OnCharInput;
import steve6472.moondust.widget.component.event.OnKeyInput;
import steve6472.moondust.widget.component.event.global.OnGlobalMouseButton;
import steve6472.moondust.widget.component.event.global.OnGlobalScroll;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 1/26/2025
 * Project: MoonDust <br>
 */
public class MoonDustCallbacks
{
    private static final Logger LOGGER = Log.getLogger(MoonDustCallbacks.class);

    public void init(WindowCallbacks callbacks, UserInput input)
    {
        // Global
        callbacks.addScrollCallback(MoonDustConstants.key("lua_global_scroll"), (_, _, yOffset) -> MoonDust.getInstance().runGlobalEvent(new OnGlobalScroll(yOffset)));
        callbacks.addMouseButtonCallback(MoonDustConstants.key("lua_global_mouse_button"), (_, button, action, mods) -> MoonDust.getInstance().runGlobalEvent(new OnGlobalMouseButton(button, action, mods)));

        // TODO: will have to disable/enable focusing with tab if in char_input component....

        callbacks.addCharCallback(MoonDustConstants.key("char_input"), (_, codepoint) -> {
            MoonDust.getInstance().getFocused().ifPresent(focused -> {
                focused.handleEvents(OnCharInput.class, _ -> true, new OnCharInput(codepoint));
            });
        });

        callbacks.addKeyCallback(MoonDustConstants.key("key_input"), (_, key, scancode, action, mods) -> {
            MoonDust.getInstance().getFocused().ifPresent(focused -> {
                focused.handleEvents(OnKeyInput.class, _ -> true, new OnKeyInput(key, scancode, action, mods));
            });
        });

//        Vector2i mousePos = input.getMousePositionRelativeToTopLeftOfTheWindow();
//        Optional<Widget> topWidgetAt = MoonDust.getInstance().getTopWidgetAt(mousePos.x, mousePos.y);
    }

}
