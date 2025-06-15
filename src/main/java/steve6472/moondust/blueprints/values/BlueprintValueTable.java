package steve6472.moondust.blueprints.values;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.blueprints.BlueprintValue;
import steve6472.moondust.blueprints.BlueprintValueType;
import steve6472.moondust.blueprints.ValidationResult;
import steve6472.moondust.core.blueprint.Blueprint;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public record BlueprintValueTable(Map<String, BlueprintValue<?>> fields, Map<String, BlueprintValue<?>> defaultValue, boolean required) implements BlueprintValue<Map<String, BlueprintValue<?>>>
{
    public static final Codec<BlueprintValueTable> CODEC_REQUIRED = RecordCodecBuilder.create(instance -> instance.group(
        Codec.unboundedMap(Codec.STRING, BlueprintValue.CODEC).fieldOf("fields").forGetter(o -> o.fields)
    ).apply(instance, v -> new BlueprintValueTable(v, null, true)));

    // TODO: this may have to be switched, idk
//    public static final Codec<BlueprintValueTable> CODEC = Codec.withAlternative(CODEC_DEFAULT, CODEC_REQUIRED);
    public static final Codec<BlueprintValueTable> CODEC = CODEC_REQUIRED;

    public BlueprintValueTable
    {

    }

    @Override
    public BlueprintValueType<Map<String, BlueprintValue<?>>, ?> getType()
    {
        return BlueprintValueType.TABLE;
    }

    @Override
    public ValidationResult validate(Map<String, BlueprintValue<?>> value)
    {
        return ValidationResult.fail("unimplemented");
    }
}
