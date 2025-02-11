package steve6472.moondust;

import steve6472.core.log.Log;
import steve6472.flare.WindowCallbacks;

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

        callbacks.addCharCallback(MoonDustConstants.key("main"), (_, codepoint) -> {

        });
    }


}
