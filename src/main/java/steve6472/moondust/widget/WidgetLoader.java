package steve6472.moondust.widget;

import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.flare.core.Flare;
import steve6472.flare.module.Module;
import steve6472.flare.util.ResourceCrawl;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.core.blueprint.DefaultBlueprint;
import steve6472.moondust.widget.blueprint.ChildrenBlueprint;
import steve6472.moondust.widget.blueprint.NameBlueprint;
import steve6472.moondust.widget.blueprint.WidgetReferenceBlueprint;
import steve6472.moondust.widget.blueprint.layout.LayoutBlueprint;
import steve6472.moondust.widget.blueprint.position.PositionBlueprint;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class WidgetLoader
{
    private static final Logger LOGGER = Log.getLogger(WidgetLoader.class);

    public static void load()
    {
        Map<Key, BlueprintFactory> factories = new LinkedHashMap<>();

        for (Module module : Flare.getModuleManager().getModules())
        {
            module.iterateNamespaces((folder, namespace) ->
            {
                File file = new File(folder, "ui/widget");
                ResourceCrawl.crawlAndLoadJsonCodec(file, MoonDustRegistries.WIDGET_BLUEPRINT.valueMapCodec(), (map, id) ->
                {
                    Key key = Key.withNamespace(namespace, id);
                    BlueprintFactory factory = createWidgetFactory(map, key);
                    LOGGER.finest("Loaded blueprint " + key + " from " + module.name());
                    factories.put(key, factory);
                });
            });
        }

        factories.values().forEach(MoonDustRegistries.WIDGET_FACTORY::register);
    }

    public static BlueprintFactory createWidgetFactory(Map<BlueprintEntry<?>, Object> map, Key key)
    {
        List<Blueprint> blueprints = new ArrayList<>(map.size());

        for (BlueprintEntry<?> blueprintEntry : map.keySet())
        {
            Object value = map.get(blueprintEntry);

            if (!(value instanceof Blueprint blueprint))
            {
                throw new RuntimeException("Not instance of Blueprint!");
            }

            blueprints.add(blueprint);
        }

        for (DefaultBlueprint<?> genericBlueprint : WidgetBlueprints.DEFAULT_BLUEPRINTS)
        {
            if (!map.containsKey(genericBlueprint.entry()))
            {
                blueprints.add(genericBlueprint.defaultValue());
            }
        }

        for (BlueprintEntry<?> requiredBlueprint : WidgetBlueprints.REQUIRED_BLUEPRINTS)
        {
            if (!map.containsKey(requiredBlueprint))
            {
                throw new RuntimeException("Widget does not contain required blueprint: '" + requiredBlueprint.key() + "'");
            }
        }

        ChildrenBlueprint children = (ChildrenBlueprint) map.get(WidgetBlueprints.CHILDREN);
        validateChildren(children, blueprints);

        return new BlueprintFactory(key, blueprints);
    }

    // Position is checked separately due to being typed and different layouts requiring different position types
    private static final Collection<Class<? extends Blueprint>> REQUIRED_CHILDREN = Set.of(WidgetReferenceBlueprint.class, NameBlueprint.class);

    private static void validateChildren(ChildrenBlueprint children, List<Blueprint> widget)
    {
        if (children == null)
            return;

        LayoutBlueprint layout = widget.stream().filter(a -> a instanceof LayoutBlueprint).map(a -> (LayoutBlueprint) a).findFirst().orElseThrow();

        for (BlueprintFactory child : children.children())
        {
            Collection<Blueprint> blueprints = child.getBlueprints();
            Collection<Blueprint> collectRequired = blueprints.stream().filter(b -> REQUIRED_CHILDREN.contains(b.getClass())).collect(Collectors.toSet());
            String name = blueprints.stream().filter(a -> a instanceof NameBlueprint).map(b -> ((NameBlueprint) b).value()).findFirst().orElse("<name component missing>");

            if (collectRequired.size() != REQUIRED_CHILDREN.size())
            {
                Collection<String> requiredStrings = REQUIRED_CHILDREN.stream().map(Class::getSimpleName).collect(Collectors.toSet());
                Collection<String> collectStrings = collectRequired.stream().map(a -> a.getClass().getSimpleName()).collect(Collectors.toSet());
                throw new IllegalStateException("Child " + name + " is missing some components, found: " + collectStrings + ", required: " + requiredStrings);
            }

            Optional<PositionBlueprint> positionOptional = blueprints.stream().filter(a -> a instanceof PositionBlueprint).map(a -> (PositionBlueprint) a).findFirst();
            positionOptional.ifPresentOrElse(position ->
            {
                if (!layout.acceptedPositionTypes().contains(position.getClass()))
                {
                    throw new IllegalStateException("Child " + name + " widget has incompatible position type (" + position.getClass().getSimpleName() + ") for layout " + layout.getClass().getSimpleName());
                }
            }, () -> {throw new IllegalStateException("Child " + name + " is missing required Position component!");});
        }
    }
}
