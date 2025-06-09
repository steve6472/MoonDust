package steve6472.moondust.blueprints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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

    public static final Codec<BlueprintStructureFields> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.unboundedMap(Codec.STRING, BlueprintValue.CODEC).fieldOf("fields").forGetter(o -> o.fields)
    ).apply(instance, BlueprintStructureFields::new));

    public BlueprintStructureFields(Map<String, BlueprintValue<?>> fields)
    {
        this.fields.putAll(fields);
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
