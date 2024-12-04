package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.core.registry.ObjectRegistry;
import steve6472.core.registry.Registry;
import steve6472.flare.registry.RegistryCreators;
import steve6472.moondust.child.ChildBlueprints;
import steve6472.moondust.panel.PanelBlueprints;
import steve6472.moondust.panel.PanelLoader;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.child.blueprint.position.PositionType;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.core.EventCallWrapper;
import steve6472.moondust.widget.WidgetBlueprints;
import steve6472.moondust.widget.WidgetLoader;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDustRegistries extends RegistryCreators
{
    /* Types */
    public static final Registry<PositionType<?>> POSITION_TYPE = createRegistry(key("position_type"), () -> PositionType.ABSOLUTE);
    public static final Registry<UIEventType<?>> EVENT_TYPE = createRegistry(key("event_type"), () -> UIEventType.RANDOM_TICK);

    public static final Registry<BlueprintEntry<?>> WIDGET_BLUEPRINT = createRegistry(key("widget_blueprint"), () -> WidgetBlueprints.SPRITES);
    public static final Registry<BlueprintEntry<?>> CHILD_BLUEPRINT = createRegistry("child_blueprint", () -> ChildBlueprints.NAME);
    public static final Registry<BlueprintEntry<?>> PANEL_BLUEPRINT = createRegistry("panel_blueprint", () -> PanelBlueprints.CHILDREN);

    public static final ObjectRegistry<BlueprintFactory> WIDGET_FACTORY = createObjectRegistry(key("widget_factory"), WidgetLoader::load);
    public static final ObjectRegistry<BlueprintFactory> PANEL_FACTORY = createObjectRegistry(key("panel_factory"), PanelLoader::load);

    public static final ObjectRegistry<EventCallWrapper<?>> EVENT_CALLS = createObjectRegistry(key("event_call"), () -> MoonDustEventCalls.Button.RANDOM_TEST);

    private static Key key(String id)
    {
        return Key.withNamespace(MoonDustConstants.NAMESPACE, id);
    }
}
