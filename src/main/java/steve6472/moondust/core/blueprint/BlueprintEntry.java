package steve6472.moondust.core.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;
import steve6472.core.registry.Serializable;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record BlueprintEntry<T extends Blueprint>(Key key, Codec<T> codec) implements Keyable, Serializable<T>
{
}
