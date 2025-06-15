package steve6472.moondust.widget.blueprint.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector2i;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.widget.component.position.RelativePos;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record RelativePosBlueprint(Vector2i offset, String parent) implements PositionBlueprint
{
    public static final Codec<RelativePosBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.VEC_2I.optionalFieldOf("offset", new Vector2i()).forGetter(RelativePosBlueprint::offset),
        Codec.STRING.fieldOf("parent").forGetter(RelativePosBlueprint::parent)
    ).apply(instance, RelativePosBlueprint::new));

    @Override
    public PositionBlueprintType<?> getType()
    {
        return PositionBlueprintType.RELATIVE;
    }

    @Override
    public List<?> createComponents()
    {
        return List.of(new RelativePos(new Vector2i(offset), parent));
    }
}
