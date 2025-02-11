package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.flare.FlareConstants;

import java.io.File;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDustConstants
{
    public static final String NAMESPACE = "moondust";

    /// Root folder for generated resources
    public static final File GENERATED_MOONDUST = new File(FlareConstants.GENERATED_FOLDER, NAMESPACE);

    public static final File MOONDUST_MODULE = new File(FlareConstants.MODULES, NAMESPACE);

    public static Key key(String id)
    {
        return Key.withNamespace(NAMESPACE, id);
    }
}
