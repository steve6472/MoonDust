package steve6472.moondust.builtin;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.builtin.blueprint.CheckBoxBlueprint;
import steve6472.moondust.builtin.blueprint.SpinnerBlueprint;
import steve6472.moondust.core.MoonDustComponentRegister;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class BuiltinBlueprints
{
    public static final BlueprintEntry<CheckBoxBlueprint> CHECKBOX = register(CheckBoxBlueprint.KEY, CheckBoxBlueprint.CODEC);
    public static final BlueprintEntry<SpinnerBlueprint> SPINNER = register(SpinnerBlueprint.KEY, SpinnerBlueprint.CODEC);

    /*
     * Register methods
     */

    private static <T extends Blueprint> BlueprintEntry<T> register(Key key, Codec<T> codec)
    {
        return MoonDustComponentRegister.register(MoonDustRegistries.WIDGET_BLUEPRINT, key, codec);
    }
}
