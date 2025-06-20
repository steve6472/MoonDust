package steve6472.moondust.blueprints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import steve6472.core.log.Log;
import steve6472.core.module.Module;
import steve6472.core.module.ResourceCrawl;
import steve6472.core.registry.Key;
import steve6472.flare.core.Flare;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.radiant.LuaTableOps;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 6/8/2025
 * Project: MoonDust <br>
 */
public class CustomBlueprintLoader
{
    private static final Logger LOGGER = Log.getLogger(CustomBlueprintLoader.class);

    public static boolean DEBUG_INPUTS = false;

    public static void load()
    {
        for (Module module : Flare.getModuleManager().getModules())
        {
            module.iterateNamespaces((folder, namespace) ->
            {
                File file = new File(folder, "ui/blueprint");
                ResourceCrawl.crawlAndLoadJsonCodec(file, BlueprintStructure.CODEC, (structure, id) ->
                {
                    Key key = Key.withNamespace(namespace, id);

                    Codec<Blueprint> CODEC = Codec.PASSTHROUGH.flatXmap(ops ->
                    {
                        Object value = ops.convert(LuaTableOps.INSTANCE).getValue();
                        ValidationResult validate = structure.validate(value);

                        if (!validate.isPass())
                        {
                            LOGGER.severe("Failed to validate blueprint '" + key + "' Reason: " + validate.getMessage());
                            return DataResult.error(() -> "Failed to validate blueprint '" + key + "' Reason: " + validate.getMessage());
                        }

                        Number number = validate.fixNumber();
                        if (number != null)
                            value = number;

                        if (DEBUG_INPUTS)
                            LOGGER.info("The input: " + value + " key: " + key);

                        return DataResult.success((Blueprint) new CustomBlueprint(structure, key, value));
                    }, _ -> null);

                    MoonDustRegistries.WIDGET_BLUEPRINT.register(key, new BlueprintEntry<>(key, CODEC));

                    LOGGER.finest("Loaded custom blueprint '" + key + "' from '" + module.name() + "'");
                });
            });
        }
    }
}
