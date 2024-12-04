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

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

                    Key key = Key.withNamespace(namespace, id);
                    BlueprintFactory factory = new BlueprintFactory(key, blueprints);
                    LOGGER.finest("Loaded blueprint " + key + " from " + module.name());
                    factories.put(key, factory);
                });
            });
        }

        factories.values().forEach(MoonDustRegistries.WIDGET_FACTORY::register);
    }
}
