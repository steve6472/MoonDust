package steve6472.moondust.blueprints.values;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.blueprints.BlueprintValue;
import steve6472.moondust.blueprints.BlueprintValueType;
import steve6472.moondust.blueprints.ValidationResult;

import java.util.regex.Pattern;

public record BlueprintValueString(int maxLength, Pattern pattern, String defaultValue, boolean required) implements BlueprintValue<String>
{
    private static final Pattern EMPTY_PATTERN = Pattern.compile("");
    private static final int MAX_LENGTH = 65535;

    public static final Codec<BlueprintValueString> CODEC_DEFAULT = RecordCodecBuilder.create(instance -> instance.group(
        Codec.intRange(0, MAX_LENGTH).optionalFieldOf("max_length", MAX_LENGTH).forGetter(BlueprintValueString::maxLength),
        Codec.STRING.xmap(Pattern::compile, Pattern::toString).optionalFieldOf("pattern", EMPTY_PATTERN).forGetter(BlueprintValueString::pattern),
        Codec.STRING.optionalFieldOf("default", "").forGetter(BlueprintValueString::defaultValue)
    ).apply(instance, (maxLen, pattern, def) -> new BlueprintValueString(maxLen, pattern, def, false)));

    public static final Codec<BlueprintValueString> CODEC_REQUIRED = RecordCodecBuilder.create(instance -> instance.group(
        Codec.intRange(0, MAX_LENGTH).optionalFieldOf("max_length", MAX_LENGTH).forGetter(BlueprintValueString::maxLength),
        Codec.STRING.xmap(Pattern::compile, Pattern::toString).optionalFieldOf("pattern", EMPTY_PATTERN).forGetter(BlueprintValueString::pattern),
        Codec.BOOL.fieldOf("required").forGetter(BlueprintValueString::required)
    ).apply(instance, (maxLen, pattern, required) -> new BlueprintValueString(maxLen, pattern, "", BlueprintValue.validateForced(required))));

    // TODO: this may have to be switched, idk
    public static final Codec<BlueprintValueString> CODEC = Codec.withAlternative(CODEC_DEFAULT, CODEC_REQUIRED);

    public BlueprintValueString
    {
        if (pattern.toString().isEmpty())
            pattern = EMPTY_PATTERN;
    }

    public boolean isPatternEmpty()
    {
        return pattern == EMPTY_PATTERN;
    }

    @Override
    public BlueprintValueType<String, ?> getType()
    {
        return BlueprintValueType.STRING;
    }

    @Override
    public ValidationResult validate(String value)
    {
        if (value.length() > maxLength)
            return ValidationResult.fail("Length is bigger than maximum allowed, %s > %s", value.length(), maxLength);

        if (!isPatternEmpty() && !pattern.matcher(value).matches())
            return ValidationResult.fail("Value does not conform to pattern %s", pattern.toString());

        return ValidationResult.PASS;
    }
}
