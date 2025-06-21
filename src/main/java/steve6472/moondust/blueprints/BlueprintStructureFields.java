package steve6472.moondust.blueprints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.blueprints.values.BlueprintValueScript;
import steve6472.moondust.luau.ImmutableLuauTable;
import steve6472.radiant.LuauTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 6/8/2025
 * Project: MoonDust <br>
 */
public class BlueprintStructureFields implements BlueprintStructure
{
    private final Map<String, BlueprintValue<?>> fields = new HashMap<>();
    private final Key script;

    public static final Codec<BlueprintStructureFields> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.unboundedMap(Codec.STRING, BlueprintValue.CODEC).fieldOf("fields").forGetter(o -> o.fields),
        Key.withDefaultNamespace(MoonDustConstants.NAMESPACE).fieldOf("script").forGetter(BlueprintStructure::script)
    ).apply(instance, BlueprintStructureFields::new));

    public BlueprintStructureFields(Map<String, BlueprintValue<?>> fields, Key script)
    {
        this.fields.putAll(fields);
        this.script = script;
    }

    @Override
    public ValidationResult validate(Object val)
    {
        if (!(val instanceof LuauTable table))
            return ValidationResult.fail("Not a table");

        for (Map.Entry<String, BlueprintValue<?>> entry : fields.entrySet())
        {
            String key = entry.getKey();
            BlueprintValue<?> value = entry.getValue();

            Object o = table.get(key);
            if (o == null)
            {
                if (fields.get(key).required())
                {
                    return ValidationResult.fail("Missing '%s' value".formatted(key));
                } else
                {
                    o = fields.get(key).defaultValue();
                    table.add(key, o);
                }
            }

            if (o instanceof Number num)
            {
                ValidationResult res = value.convertNumericAndValidate(num);
                if (!res.isPass())
                    return res;

                Number number = res.fixNumber();
                if (number != null)
                    table.add(key, number);
            } else
            {
                if (value instanceof BlueprintValueScript)
                {
                    if (o instanceof String)
                        o = ImmutableLuauTable.of("script", o, "input", new ImmutableLuauTable());
                }
                //noinspection unchecked
                ValidationResult res = ((BlueprintValue<Object>) value).validate(o);
                if (!res.isPass())
                    return res;
            }
        }
        return ValidationResult.PASS;
    }

    @Override
    public Key script()
    {
        return script;
    }

    @Override
    public boolean isInline()
    {
        return false;
    }

    @Override
    public String toString()
    {
        return "BlueprintStructureFields{" + "fields=" + fields + '}';
    }
}
