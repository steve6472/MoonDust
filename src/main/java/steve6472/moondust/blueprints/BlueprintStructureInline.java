package steve6472.moondust.blueprints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDust;
import steve6472.moondust.MoonDustConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 6/8/2025
 * Project: MoonDust <br>
 */
public class BlueprintStructureInline implements BlueprintStructure
{
    private final BlueprintValue<?> value;
    private final Key script;

    public static final Codec<BlueprintStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlueprintValue.CODEC.fieldOf("value").forGetter(o -> ((BlueprintStructureInline) o).value),
        Key.withDefaultNamespace(MoonDustConstants.NAMESPACE).fieldOf("script").forGetter(BlueprintStructure::script)
    ).apply(instance, BlueprintStructureInline::new));

    public BlueprintStructureInline(BlueprintValue<?> value, Key script)
    {
        this.value = value;
        this.script = script;
    }

    @Override
    public ValidationResult validate(Object val)
    {
        if (val instanceof Number num)
        {
            return value.convertNumericAndValidate(num);
        }
        //noinspection unchecked
        return ((BlueprintValue<Object>) value).validate(val);
    }

    @Override
    public Key script()
    {
        return script;
    }

    @Override
    public boolean isInline()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "BlueprintStructureInline{" + "value=" + value + '}';
    }
}
