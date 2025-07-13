package steve6472.moondust.blueprints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import steve6472.core.log.Log;
import steve6472.core.module.Module;
import steve6472.core.module.ResourceCrawl;
import steve6472.core.registry.Key;
import steve6472.flare.core.Flare;
import steve6472.moondust.MoonDustParts;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.BlueprintFactory;
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

    public static boolean DEBUG_INPUTS_BEFORE = false;
    public static boolean DEBUG_INPUTS_AFTER = false;

    public static void load()
    {
        Flare.getModuleManager().loadParts(MoonDustParts.UI_BLUEPRINT, BlueprintStructure.CODEC, (structure, key) ->
        {
            Codec<Blueprint> CODEC = Codec.PASSTHROUGH.flatXmap(ops ->
            {
                Object value = ops.convert(LuaTableOps.INSTANCE).getValue();
                if (DEBUG_INPUTS_BEFORE)
                    LOGGER.info("The input before validation: " + value + " key: " + key);

                ValidationResult validate;
                try
                {
                    validate = structure.validate(value);
                } catch (Exception exception)
                {
                    LOGGER.severe("Error while validating custom blueprint: " + structure.toString() + " with input: " + value);
                    throw exception;
                }

                if (!validate.isPass())
                {
                    LOGGER.severe("Failed to validate blueprint '" + key + "' Reason: " + validate.getMessage());
                    return DataResult.error(() -> "Failed to validate blueprint '" + key + "' Reason: " + validate.getMessage());
                }

                Number number = validate.fixNumber();
                if (number != null)
                    value = number;

                if (DEBUG_INPUTS_AFTER)
                    LOGGER.info("The input after: " + value + " key: " + key);

                return DataResult.success((Blueprint) new CustomBlueprint(structure, key, value));
            }, _ -> null);

            MoonDustRegistries.WIDGET_BLUEPRINT.register(key, new BlueprintEntry<>(key, CODEC));
        });
    }
}
