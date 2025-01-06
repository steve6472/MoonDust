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
public interface MoonDustKeybinds
{
    Keybind BACK_MODIFIER = register(new Keybind(Key.withNamespace(MoonDustConstants.NAMESPACE, "back_modifier"), KeybindType.REPEAT, GLFW.GLFW_KEY_LEFT_SHIFT));
    Keybind NEXT_WIDGET = register(new Keybind(Key.withNamespace(MoonDustConstants.NAMESPACE, "next_widget"), KeybindType.ONCE, GLFW.GLFW_KEY_TAB));
    Keybind UNFOCUS_ALL = register(new Keybind(Key.withNamespace(MoonDustConstants.NAMESPACE, "unfocus_all"), KeybindType.ONCE, GLFW.GLFW_KEY_ESCAPE));
    Keybind CONFIRM = register(new Keybind(Key.withNamespace(MoonDustConstants.NAMESPACE, "confirm"), KeybindType.ONCE, GLFW.GLFW_KEY_SPACE));

    private static Keybind register(Keybind keybind)
    {
        MoonDustRegistries.KEYBIND.register(keybind);
        return keybind;
    }
}
