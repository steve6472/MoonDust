package steve6472.moondust.blueprints;

import steve6472.core.log.Log;
import steve6472.core.module.Module;
import steve6472.core.module.ResourceCrawl;
import steve6472.core.registry.Key;
import steve6472.flare.core.Flare;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.widget.WidgetLoader;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 6/8/2025
 * Project: MoonDust <br>
 */
public class CustomBlueprintLoader
{
    private static final Logger LOGGER = Log.getLogger(CustomBlueprintLoader.class);

    public static void load()
    {
        Map<Key, BlueprintFactory> factories = new LinkedHashMap<>();

        for (Module module : Flare.getModuleManager().getModules())
        {
            module.iterateNamespaces((folder, namespace) ->
            {
                File file = new File(folder, "ui/blueprint");
                ResourceCrawl.crawlAndLoadJsonCodec(file, BlueprintStructure.CODEC, (structure, id) ->
                {
                    Key key = Key.withNamespace(namespace, id);
                    LOGGER.info("" + structure);
                    LOGGER.finest("Loaded custom blueprint " + key + " from " + module.name());
                });
            });
        }

//        factories.values().forEach(MoonDustRegistries.WIDGET_FACTORY::register);
    }
}
