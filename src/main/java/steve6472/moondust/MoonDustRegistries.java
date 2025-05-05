package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.core.registry.ObjectRegistry;
import steve6472.core.registry.Registry;
import steve6472.flare.input.Keybind;
import steve6472.flare.registry.RegistryCreators;
import steve6472.moondust.builtin.BuiltinBlueprints;
import steve6472.moondust.core.MoonDustKeybinds;
import steve6472.moondust.luau.LuaScriptLoader;
import steve6472.moondust.luau.ProfiledScript;
import steve6472.moondust.widget.BlueprintOverrides;
import steve6472.moondust.widget.MoonDustComponents;
import steve6472.moondust.widget.blueprint.layout.LayoutType;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.blueprint.position.PositionType;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.widget.WidgetBlueprints;
import steve6472.moondust.widget.WidgetLoader;
import steve6472.moondust.widget.component.event.UIEventCall;
import steve6472.moondust.widget.override.OverrideEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDustRegistries extends RegistryCreators
{
    static
    {
        MoonDustComponents.init();
    }

    /* Types */
    public static final Registry<PositionType<?>> POSITION_TYPE = createRegistry(key("position_type"), () -> PositionType.ABSOLUTE);
    public static final Registry<LayoutType<?>> LAYOUT_TYPE = createRegistry(key("layout_type"), () -> LayoutType.ABSOLUTE);

    public static final Registry<UIEventType<?>> EVENT_TYPE = createRegistry(key("event_type"), () -> UIEventType.ON_RANDOM_TICK);

    public static final Registry<OverrideEntry<?>> OVERRIDE = createRegistry(key("override"), () -> BlueprintOverrides.EVENTS);
    public static final Registry<BlueprintEntry<?>> WIDGET_BLUEPRINT = createRegistry(key("widget_blueprint"), () ->
    {
        // Ugly thing to make IDE shut up while still properly initializing entries for the registry
        var _ = WidgetBlueprints.SPRITES.key();
        var _ = BuiltinBlueprints.BUTTON.key();
    });

    public static final ObjectRegistry<BlueprintFactory> WIDGET_FACTORY = createObjectRegistry(key("widget_factory"), WidgetLoader::load);
    public static final ObjectRegistry<ProfiledScript> LUA_SCRIPTS = createObjectRegistry(key("lua_scripts"), LuaScriptLoader::load);

    public static final Map<Key, UIEventCall<?>> EVENT_CALLS = new HashMap<>();

    public static final Registry<Keybind> KEYBIND = createRegistry(key("keybind"), () -> MoonDustKeybinds.BACK_MODIFIER);

    private static Key key(String id)
    {
        return Key.withNamespace(MoonDustConstants.NAMESPACE, id);
    }
}
