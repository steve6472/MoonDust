package steve6472.moondust.blueprints;

import com.mojang.serialization.Codec;

import steve6472.core.registry.Key;
import steve6472.core.registry.Typed;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;

public interface BlueprintValue<V> extends Typed<BlueprintValueType<V, ?>>
{
    Key KEY = MoonDustConstants.key("value");
    Codec<BlueprintValue<?>> CODEC = MoonDustRegistries.BLUEPRINT_VALUE_TYPE.byKeyCodec().dispatch("type", BlueprintValue::getType, BlueprintValueType::mapCodec);

    // TODO: format this better lol
    // TODO: maybe put this in BlueprintValue
    /// if true -> the presence in the custom blueprint is required and default is ignored
    ///         -> if it is missing, throws error
    /// if false -> if the value is missing from custom blueprint the default is used
    /// Throws error if required and default are together in value definition (simply, the codec for this should never exist)
    boolean required();

    V defaultValue();

    /// Because Codecs decode 2 as Byte
    default V convertNumeric(Number number)
    {
        throw new IllegalStateException("Unimplemented method");
    }

    ValidationResult validate(V value);

    default ValidationResult convertNumericAndValidate(Number number)
    {
        V fixedNumber = convertNumeric(number);
        if (!(fixedNumber instanceof Number))
            throw new IllegalStateException("Conversion did not result in a number");
        ValidationResult result = validate(fixedNumber);
        return result.withNumberFix((Number) fixedNumber);
    }

    /// TODO: add doc
    static boolean validateForced(boolean required)
    {
        // TODO: do not throw runtime, man
        if (!required)
            throw new RuntimeException("required can only be true");

        return true;
    }
}
