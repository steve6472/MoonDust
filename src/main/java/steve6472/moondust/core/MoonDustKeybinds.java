package steve6472.moondust.core;

import org.lwjgl.glfw.GLFW;
import steve6472.core.registry.Key;
import steve6472.flare.input.Keybind;
import steve6472.flare.input.KeybindType;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class MoonDustKeybinds
{
    public static final Keybind BACK_MODIFIER = register(new Keybind(Key.withNamespace(MoonDustConstants.NAMESPACE, "back_modifier"), KeybindType.REPEAT, GLFW.GLFW_KEY_LEFT_SHIFT));
    public static final Keybind NEXT_WIDGET = register(new Keybind(Key.withNamespace(MoonDustConstants.NAMESPACE, "next_widget"), KeybindType.ONCE, GLFW.GLFW_KEY_TAB));
    public static final Keybind UNFOCUS_ALL = register(new Keybind(Key.withNamespace(MoonDustConstants.NAMESPACE, "unfocus_all"), KeybindType.ONCE, GLFW.GLFW_KEY_ESCAPE));

    private static Keybind register(Keybind keybind) {
        MoonDustRegistries.KEYBIND.register(keybind);
        return keybind;
    }
}