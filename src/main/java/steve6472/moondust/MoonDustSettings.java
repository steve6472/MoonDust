package steve6472.moondust;

import steve6472.core.setting.BoolSetting;
import steve6472.core.setting.SettingRegister;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDustSettings extends SettingRegister
{
    static {
        REGISTRY = MoonDustRegistries.SETTINGS;
        NAMESPACE = MoonDustConstants.NAMESPACE;
    }

    /*
     * Debug
     */

    /// Shows green pixel below cursor, locked to pixel grid
    public static final BoolSetting DEBUG_CURSOR = registerBool("debug_cursor", false);
}
