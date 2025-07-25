package steve6472.moondust.blueprints;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;

/**
 * Created by steve6472
 * Date: 6/9/2025
 * Project: MoonDust <br>
 */
public interface BlueprintStructure
{
    Codec<BlueprintStructure> CODEC = Codec.withAlternative(BlueprintStructureInline.CODEC, BlueprintStructureFields.CODEC);

    /// empty as I don't have the object to be passed in yet
    ValidationResult validate(Object value);

    Key script();

    boolean isInline();
}
