package steve6472.moondust;

import steve6472.core.log.Log;
import steve6472.flare.WindowCallbacks;
import steve6472.moondust.widget.component.event.OnCharInput;
import steve6472.moondust.widget.component.event.OnKeyInput;

import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 1/26/2025
 * Project: MoonDust <br>
 */
public class MoonDustCallbacks
{
    private static final Logger LOGGER = Log.getLogger(MoonDustCallbacks.class);

    public void init(WindowCallbacks callbacks)
    {
        callbacks.addScrollCallback(MoonDustConstants.key("main"), (_, xOffset, yOffset) -> {
            LOGGER.info("Scroll " + xOffset + "/" + yOffset);
        });

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
    }


}
