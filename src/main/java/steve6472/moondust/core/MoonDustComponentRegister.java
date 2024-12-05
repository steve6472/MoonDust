package steve6472.moondust.core;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Registry;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.DefaultBlueprint;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/5/2024
 * Project: MoonDust <br>
 */
public final class MoonDustComponentRegister
{
    public static <T extends Blueprint> BlueprintEntry<T> register(Registry<BlueprintEntry<?>> registry, Key key, Codec<T> codec)
    {
        if (registry.get(key) != null)
            throw new RuntimeException("Blueprint with key " + key + " already exists!");

        BlueprintEntry<T> obj = new BlueprintEntry<>(key, codec);
        registry.register(key, obj);
        return obj;
    }

    public static <T extends Blueprint> BlueprintEntry<T> registerRequired(Registry<BlueprintEntry<?>> registry, List<BlueprintEntry<?>> requiredBlueprints, Key key, Codec<T> codec)
    {
        BlueprintEntry<T> register = register(registry, key, codec);
        requiredBlueprints.add(register);
        return register;
    }

    public static <T extends Blueprint> BlueprintEntry<T> registerDefault(Registry<BlueprintEntry<?>> registry, List<DefaultBlueprint<?>> defaultBlueprints, Key key, Codec<T> codec, T defaultValue)
    {
        BlueprintEntry<T> register = register(registry, key, codec);
        defaultBlueprints.add(new DefaultBlueprint<>(register, defaultValue));
        return register;
    }
}
