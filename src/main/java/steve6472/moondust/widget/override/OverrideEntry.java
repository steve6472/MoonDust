package steve6472.moondust.widget.override;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;
import steve6472.core.registry.Serializable;
import steve6472.moondust.widget.BlueprintOverride;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record OverrideEntry<T extends BlueprintOverride<?>>(Key key, Codec<T> codec) implements Keyable, Serializable<T>
{
}
