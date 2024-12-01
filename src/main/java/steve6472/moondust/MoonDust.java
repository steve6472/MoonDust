package steve6472.moondust;

import steve6472.flare.FlareExport;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDust
{
    /// Should be called before Flare.start()
    public static void init() throws IOException, URISyntaxException
    {
        FlareExport.exportFolder("moondust/module", MoonDustConstants.MOONDUST_MODULE);
    }
}
