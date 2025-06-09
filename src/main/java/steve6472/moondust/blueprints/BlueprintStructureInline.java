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
public class BlueprintStructureInline implements BlueprintStructure
{
    private final BlueprintValue<?> value;

    public static final Codec<BlueprintStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlueprintValue.CODEC.fieldOf("value").forGetter(o -> ((BlueprintStructureInline) o).value)
    ).apply(instance, BlueprintStructureInline::new));

    public BlueprintStructureInline(BlueprintValue<?> value)
    {
        this.value = value;
    }

    public boolean validate()
    {
        return true;
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
