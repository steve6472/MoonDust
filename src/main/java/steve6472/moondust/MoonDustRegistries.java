package steve6472.moondust;

import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.core.registry.ObjectRegistry;
import steve6472.core.registry.Registry;
import steve6472.flare.input.Keybind;
import steve6472.flare.registry.RegistryCreators;
import steve6472.moondust.blueprints.BlueprintValueType;
import steve6472.moondust.blueprints.CustomBlueprintLoader;
import steve6472.moondust.builtin.BuiltinBlueprints;
import steve6472.moondust.core.MoonDustKeybinds;
import steve6472.moondust.luau.LuaScriptLoader;
import steve6472.moondust.luau.ProfiledScript;
import steve6472.moondust.widget.BlueprintOverrides;
import steve6472.moondust.widget.MoonDustComponents;
import steve6472.moondust.widget.blueprint.layout.LayoutType;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.blueprint.position.PositionBlueprintType;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.widget.WidgetBlueprints;
import steve6472.moondust.widget.WidgetLoader;
import steve6472.moondust.widget.component.event.UIEventCall;
import steve6472.moondust.widget.component.position.PositionType;
import steve6472.moondust.widget.override.OverrideEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDustRegistries extends RegistryCreators
{
    private static final Logger LOGGER = Log.getLogger(MoonDustRegistries.class);

    static
    {
        LOADERS.put(MoonDustConstants.key("moondust_components"), MoonDustComponents::init);
    }

    /* Types */
    public static final Registry<PositionBlueprintType<?>> POSITION_BLUEPRINT_TYPE = createNamespacedRegistry(MoonDustConstants.NAMESPACE, key("position_blueprint_type"), () -> PositionBlueprintType.ABSOLUTE);
    public static final Registry<PositionType<?>> POSITION_TYPE = createNamespacedRegistry(MoonDustConstants.NAMESPACE, key("position_type"), () -> PositionType.ABSOLUTE);
    public static final Registry<LayoutType<?>> LAYOUT_TYPE = createNamespacedRegistry(MoonDustConstants.NAMESPACE, key("layout_type"), () -> LayoutType.ABSOLUTE);
    public static final Registry<BlueprintValueType<?, ?>> BLUEPRINT_VALUE_TYPE = createNamespacedRegistry(MoonDustConstants.NAMESPACE, key("blueprint_value_type"), () -> BlueprintValueType.INT);

    public static final Registry<UIEventType<?>> EVENT_TYPE = createNamespacedRegistry(MoonDustConstants.NAMESPACE, key("event_type"), () -> UIEventType.ON_RANDOM_TICK);

    public static final Registry<OverrideEntry<?>> OVERRIDE = createNamespacedRegistry(MoonDustConstants.NAMESPACE, key("override"), () -> BlueprintOverrides.EVENTS);
    public static final Registry<BlueprintEntry<?>> WIDGET_BLUEPRINT = createNamespacedRegistry(MoonDustConstants.NAMESPACE, key("widget_blueprint"), () ->
    {
        LOGGER.finest("Bootstrapping '%s'".formatted(WidgetBlueprints.SPRITES.key()));
        LOGGER.finest("Bootstrapping '%s'".formatted(BuiltinBlueprints.BUTTON.key()));
        LOGGER.finest("Bootstrapping '%s'".formatted("Custom Blueprints"));
        // Ugly thing to make IDE shut up while still properly initializing entries for the registry
        CustomBlueprintLoader.load();
    });

    public static final ObjectRegistry<BlueprintFactory> WIDGET_FACTORY = createObjectRegistry(key("widget_factory"), WidgetLoader::load);
    public static final ObjectRegistry<ProfiledScript> LUA_SCRIPTS = createObjectRegistry(key("lua_script"), LuaScriptLoader::load);

    public static final Map<Key, UIEventCall<?>> EVENT_CALLS = new HashMap<>();

    public static final Registry<Keybind> KEYBIND = createNamespacedRegistry(MoonDustConstants.NAMESPACE, key("keybind"), () -> MoonDustKeybinds.BACK_MODIFIER);

    private static Key key(String id)
    {
        return Key.withNamespace(MoonDustConstants.NAMESPACE, id);
    }
}
