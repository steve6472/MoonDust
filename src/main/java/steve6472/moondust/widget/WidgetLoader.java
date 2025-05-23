package steve6472.moondust.widget;

import org.joml.Vector2i;
import steve6472.core.log.Log;
import steve6472.core.module.Module;
import steve6472.core.module.ResourceCrawl;
import steve6472.core.registry.Key;
import steve6472.flare.core.Flare;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.core.blueprint.DefaultBlueprint;
import steve6472.moondust.widget.blueprint.*;
import steve6472.moondust.widget.blueprint.event.EventsBlueprint;
import steve6472.moondust.widget.blueprint.flag.ClickableBlueprint;
import steve6472.moondust.widget.blueprint.flag.FocusableBlueprint;
import steve6472.moondust.widget.blueprint.layout.LayoutBlueprint;
import steve6472.moondust.widget.blueprint.position.PositionBlueprint;
import steve6472.moondust.widget.component.flag.Clickable;

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
                    BlueprintFactory factory = createWidgetFactory(map, true, key);
                    LOGGER.finest("Loaded blueprint " + key + " from " + module.name());
                    factories.put(key, factory);
                });
            });
        }

        factories.values().forEach(MoonDustRegistries.WIDGET_FACTORY::register);
    }

    public static BlueprintFactory createWidgetFactory(Map<BlueprintEntry<?>, Object> map, boolean includeDefault, Key key)
    {
        Map<Key, Blueprint> blueprints = new LinkedHashMap<>(map.size());

        for (BlueprintEntry<?> blueprintEntry : map.keySet())
        {
            Object value = map.get(blueprintEntry);

            if (!(value instanceof Blueprint blueprint))
            {
                throw new RuntimeException("Not instance of Blueprint!");
            }

            blueprints.put(blueprintEntry.key(), blueprint);
        }

        if (includeDefault)
        {
            for (DefaultBlueprint<?> genericBlueprint : WidgetBlueprints.DEFAULT_BLUEPRINTS)
            {
                if (!map.containsKey(genericBlueprint.entry()))
                {
                    blueprints.put(genericBlueprint.entry().key(), genericBlueprint.defaultValue());
                } else
                {
                    Object current = map.get(genericBlueprint.entry());
                    if (current.equals(genericBlueprint.defaultValue()))
                    {
                        LOGGER.fine("Pointless declaration of default component '" + genericBlueprint.entry().key() + "' with default value '" + current + "' in: " + key);
                    }
                }
            }
        }

        ChildrenBlueprint children = (ChildrenBlueprint) map.get(WidgetBlueprints.CHILDREN);
        validateChildren(children, blueprints);

        // TODO: this can override components from widget reference, is this correct ? (I don't think it should be called from child)
        fillSpecialDefaults(blueprints, key);

        return new BlueprintFactory(key, blueprints);
    }

    // TODO: add special defaults/verification in a bit more expandable way
    private static void fillSpecialDefaults(Map<Key, Blueprint> blueprints, Key key)
    {
        // Add sprite_size if it is not defined, uses size size if defined, otherwise nothing is added
        find(blueprints, SpriteSizeBlueprint.class).ifPresentOrElse(spriteSize -> {
            find(blueprints, BoundsBlueprint.class).ifPresent(bounds -> {
                if (spriteSize.size().equals(bounds.bounds()))
                    LOGGER.fine("Pointless declaration of '%s', it has same size as '%s', value: %s in: %s".formatted(WidgetBlueprints.SPRITE_SIZE.key(), WidgetBlueprints.BOUNDS.key(), bounds.bounds(), key));
            });
        }, () -> {
            find(blueprints, BoundsBlueprint.class).ifPresent(bounds -> {
                blueprints.put(SpriteSizeBlueprint.KEY, new SpriteSizeBlueprint(new Vector2i(bounds.bounds())));
            });
        });

        // Add clickbox_size if clickable is defined as yes, use bounds size as default if defined
        find(blueprints, ClickboxSizeBlueprint.class).ifPresentOrElse(clickboxSize -> {
            if (find(blueprints, ClickableBlueprint.class).orElseThrow().state() == Clickable.NO)
            {
                LOGGER.fine("'%s' defined, but widget is not clickable. in: %s".formatted(WidgetBlueprints.CLICKBOX_SIZE.key(), key));
            }
            find(blueprints, BoundsBlueprint.class).ifPresent(bounds -> {
                if (clickboxSize.size().equals(bounds.bounds()))
                    LOGGER.fine("Pointless declaration of '%s', it has same size as '%s', value: %s in: %s".formatted(WidgetBlueprints.CLICKBOX_SIZE.key(), WidgetBlueprints.BOUNDS.key(), bounds.bounds(), key));
            });
        }, () -> {
            find(blueprints, BoundsBlueprint.class).ifPresent(bounds -> {
                find(blueprints, ClickableBlueprint.class).ifPresent(clickable -> {
                    if (clickable.state() == Clickable.YES)
                        blueprints.put(ClickboxSizeBlueprint.KEY, new ClickboxSizeBlueprint(new Vector2i(bounds.bounds())));
                });
            });
        });

        find(blueprints, FocusableBlueprint.class).ifPresent(focusable -> {
            if (focusable.state().flag())
            {
                find(blueprints, FocusedSpriteBlueprint.class).ifPresentOrElse(_ -> {}, () -> {
                    LOGGER.warning(String.format("Widget '%s' is focusable, but lacks '%s' blueprint!", key, WidgetBlueprints.FOCUSED_SPRITE.key()));
                });
            }
        });

        find(blueprints, EventsBlueprint.class).ifPresent(_ -> LOGGER.warning("Widget %s contains events component, these will be removed in the future!".formatted(key)));
    }

    // Position is checked separately due to being typed and different layouts requiring different position types
    private static final Collection<Class<? extends Blueprint>> REQUIRED_CHILDREN = Set.of(WidgetReferenceBlueprint.class, NameBlueprint.class);

    private static void validateChildren(ChildrenBlueprint children, Map<Key, Blueprint> widget)
    {
        if (children == null)
            return;

        Optional<LayoutBlueprint> layoutBlueprint = find(widget, LayoutBlueprint.class);
        if (layoutBlueprint.isEmpty())
            throw new IllegalStateException("Layout Blueprint is required");
        LayoutBlueprint layout = layoutBlueprint.orElseThrow();

        for (BlueprintFactory child : children.children())
        {
            Map<Key, Blueprint> blueprints = child.blueprints();
            Collection<Blueprint> collectRequired = blueprints.values().stream().filter(b -> REQUIRED_CHILDREN.contains(b.getClass())).collect(Collectors.toSet());
            String name = blueprints.values().stream().filter(a -> a instanceof NameBlueprint).map(b -> ((NameBlueprint) b).value()).findFirst().orElse("<name component missing>");

            if (collectRequired.size() != REQUIRED_CHILDREN.size())
            {
                Collection<String> requiredStrings = REQUIRED_CHILDREN.stream().map(Class::getSimpleName).collect(Collectors.toSet());
                Collection<String> collectStrings = collectRequired.stream().map(a -> a.getClass().getSimpleName()).collect(Collectors.toSet());
                throw new IllegalStateException("Child '" + name + "' is missing some components, found: " + collectStrings + ", required: " + requiredStrings);
            }

            find(blueprints, PositionBlueprint.class).ifPresentOrElse(position ->
            {
                if (!layout.acceptedPositionTypes().contains(position.getClass()))
                {
                    throw new IllegalStateException("Child '" + name + "' widget has incompatible position type (" + position.getClass().getSimpleName() + ") for layout " + layout.getClass().getSimpleName());
                }
            }, () -> {throw new IllegalStateException("Child '" + name + "' is missing required Position component!");});
        }
    }

    /// Util function to shorten code to\
    /// Finds first type in a list\
    /// Returns optional
    private static <T extends Blueprint> Optional<T> find(Map<Key, Blueprint> list, Class<T> type)
    {
        //noinspection unchecked
        return list
            .values()
            .stream()
            .filter(a ->
            {
                if (type.isInterface())
                    return Set.of(a.getClass().getInterfaces()).contains(type);
                return a.getClass().equals(type);
            })
            .map(b -> (T) b)
            .findFirst();
    }
}
