package steve6472.moondust.blueprints.values;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import steve6472.moondust.blueprints.BlueprintValidationException;
import steve6472.moondust.blueprints.BlueprintValue;
import steve6472.moondust.blueprints.BlueprintValueType;
import steve6472.moondust.blueprints.ValidationResult;

public record BlueprintValueInt(int min, int max, int defaultValue, boolean required) implements BlueprintValue<Integer>
{
    public static final Codec<BlueprintValueInt> CODEC_DEFAULT = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(BlueprintValueInt::min),
        Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(BlueprintValueInt::max),
        Codec.INT.optionalFieldOf("default", 0).forGetter(BlueprintValueInt::defaultValue)
    ).apply(instance, (min, max, def) -> new BlueprintValueInt(min, max, def, false)));

    public static final Codec<BlueprintValueInt> CODEC_REQUIRED = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(BlueprintValueInt::min),
        Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(BlueprintValueInt::max),
        Codec.BOOL.fieldOf("required").forGetter(BlueprintValueInt::required)
    ).apply(instance, (min, max, required) -> new BlueprintValueInt(min, max, 0, BlueprintValue.validateForced(required))));
    
    // TODO: this may have to be switched, idk
    public static final Codec<BlueprintValueInt> CODEC = Codec.withAlternative(CODEC_DEFAULT, CODEC_REQUIRED);

    public BlueprintValueInt 
    {
        // TODO: do not throw runtime, it's not runtime
        if (min > max)
            throw new RuntimeException("min is bigger than max, this is not allowed (%s > %s)".formatted(min, max));
    }

    @Override
    public BlueprintValueType<Integer, ?> getType()
    {
        return BlueprintValueType.INT;
    }

    @Override
    public Integer convertNumeric(Number number)
    {
        return number.intValue();
    }

    @Override
    public ValidationResult validate(Integer value)
    {
        int val = value;
        if (val < min)
            return ValidationResult.fail("Value is lower than minimum (%s < %s)", val, min);
        if (val > max)
            return ValidationResult.fail("Value is higher than maximum (%s > %s)", val, max);

        return ValidationResult.PASS;
    }
}
