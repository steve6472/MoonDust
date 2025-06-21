package steve6472.moondust.blueprints.values;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.blueprints.BlueprintValue;
import steve6472.moondust.blueprints.BlueprintValueType;
import steve6472.moondust.blueprints.ValidationResult;

import java.util.List;
import java.util.regex.Pattern;

public record BlueprintValueStringArray(int maxSize, String[] defaultValue, boolean required) implements BlueprintValue<String[]>
{
    private static final int MAX_SIZE = 65535;

    private static final Codec<String[]> STRING_ARRAY = Codec.STRING.listOf().xmap(list -> list.toArray(new String[0]), List::of);

    public static final Codec<BlueprintValueStringArray> CODEC_DEFAULT = RecordCodecBuilder.create(instance -> instance.group(
        Codec.intRange(0, MAX_SIZE).optionalFieldOf("max_size", MAX_SIZE).forGetter(BlueprintValueStringArray::maxSize),
        STRING_ARRAY.optionalFieldOf("default", new String[0]).forGetter(BlueprintValueStringArray::defaultValue)
    ).apply(instance, (maxLen, def) -> new BlueprintValueStringArray(maxLen, def, false)));

    public static final Codec<BlueprintValueStringArray> CODEC_REQUIRED = RecordCodecBuilder.create(instance -> instance.group(
        Codec.intRange(0, MAX_SIZE).optionalFieldOf("max_size", MAX_SIZE).forGetter(BlueprintValueStringArray::maxSize),
        Codec.BOOL.fieldOf("required").forGetter(BlueprintValueStringArray::required)
    ).apply(instance, (maxLen, required) -> new BlueprintValueStringArray(maxLen, new String[0], BlueprintValue.validateForced(required))));

    public static final Codec<BlueprintValueStringArray> CODEC = Codec.withAlternative(CODEC_DEFAULT, CODEC_REQUIRED);

    @Override
    public BlueprintValueType<String[], ?> getType()
    {
        return BlueprintValueType.STRING_ARRAY;
    }

    @Override
    public ValidationResult validate(String[] value)
    {
        if (value.length > maxSize)
            return ValidationResult.fail("Array size is bigger than maximum allowed, %s > %s", value.length, maxSize);

        return ValidationResult.PASS;
    }
}
