package steve6472.moondust.blueprints.values;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.blueprints.BlueprintValue;
import steve6472.moondust.blueprints.BlueprintValueType;
import steve6472.moondust.blueprints.ValidationResult;

public record BlueprintValueBool(boolean defaultValue, boolean required) implements BlueprintValue<Boolean>
{
    public static final Codec<BlueprintValueBool> CODEC_DEFAULT = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("default", false).forGetter(BlueprintValueBool::defaultValue)
    ).apply(instance, def -> new BlueprintValueBool(def, false)));

    public static final Codec<BlueprintValueBool> CODEC_REQUIRED = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.fieldOf("required").forGetter(BlueprintValueBool::required)
    ).apply(instance, required -> new BlueprintValueBool(false, BlueprintValue.validateForced(required))));

    // TODO: this may have to be switched, idk
    public static final Codec<BlueprintValueBool> CODEC = Codec.withAlternative(CODEC_DEFAULT, CODEC_REQUIRED);

    @Override
    public BlueprintValueType<Boolean, ?> getType()
    {
        return BlueprintValueType.BOOL;
    }

    @Override
    public ValidationResult validate(Boolean value)
    {
        return ValidationResult.PASS;
    }
}
