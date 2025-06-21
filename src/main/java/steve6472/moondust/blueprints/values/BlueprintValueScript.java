package steve6472.moondust.blueprints.values;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.blueprints.BlueprintValue;
import steve6472.moondust.blueprints.BlueprintValueType;
import steve6472.moondust.blueprints.ValidationResult;
import steve6472.moondust.luau.ImmutableLuauTable;
import steve6472.moondust.luau.ProfiledScript;
import steve6472.moondust.widget.blueprint.ScriptEntry;
import steve6472.radiant.LuauTable;

public record BlueprintValueScript(LuauTable defaultValue, boolean required) implements BlueprintValue<LuauTable>
{
    private static final Key MISSINGNO = MoonDustConstants.key("missingno");
    private static final LuauTable EMPTY = ImmutableLuauTable.of("script", MISSINGNO.toString(), "input", new ImmutableLuauTable());

    private static final Codec<LuauTable> SCRIPT_ENTRY_INPUT_CODEC = ScriptEntry.CODEC.flatComapMap(o -> ImmutableLuauTable.of("script", o.script().toString(), "input", o.input()), t -> {
        Object script = t.get("script");
        Object input = t.get("input");

        if (script == null)
            return DataResult.error(() -> "No 'script' key found!");
        if (!(script instanceof String scriptStr))
            return DataResult.error(() -> "Key 'script' is not a string");
        if (input == null)
            return DataResult.success(new ScriptEntry(Key.parse(scriptStr)));
        return DataResult.success(new ScriptEntry(Key.parse(scriptStr), input));
    });

//    private static final Codec<LuauTable> SCRIPT_ENTRY_CODEC = Codec.withAlternative(SCRIPT_ENTRY_INPUT_CODEC, Codec.STRING, str -> ImmutableLuauTable.of("script", str, "input", new ImmutableLuauTable()));

    public static final Codec<BlueprintValueScript> CODEC_DEFAULT = RecordCodecBuilder.create(instance -> instance.group(
        SCRIPT_ENTRY_INPUT_CODEC.optionalFieldOf("default", EMPTY).forGetter(BlueprintValueScript::defaultValue)
    ).apply(instance, (def) -> new BlueprintValueScript(def, false)));

    public static final Codec<BlueprintValueScript> CODEC_REQUIRED = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.fieldOf("required").forGetter(BlueprintValueScript::required)
    ).apply(instance, (required) -> new BlueprintValueScript(EMPTY, BlueprintValue.validateForced(required))));

    public static final Codec<BlueprintValueScript> CODEC = Codec.withAlternative(CODEC_DEFAULT, CODEC_REQUIRED);

    @Override
    public BlueprintValueType<LuauTable, ?> getType()
    {
        return BlueprintValueType.SCRIPT;
    }

    @Override
    public ValidationResult validate(LuauTable value)
    {
        Object o = value.get("script");
        if (!(o instanceof String scriptStr))
            return ValidationResult.fail("Key 'script' is not string or does not exist");
        Key scriptKey = Key.parse(scriptStr);

        ProfiledScript profiledScript = MoonDustRegistries.LUA_SCRIPT.get(scriptKey);
        if (profiledScript == null && !scriptKey.equals(MISSINGNO))
            return ValidationResult.fail("Could not find lua script '%s'", scriptKey);

        return ValidationResult.PASS;
    }
}
