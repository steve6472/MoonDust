package steve6472.moondust.widget.blueprint.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector2i;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.widget.component.position.AnchoredPos;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record AnchoredPosBlueprint(Vector2i offset, AnchoredPos.Anchor anchor) implements PositionBlueprint
{
    public static final Codec<AnchoredPosBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.VEC_2I.optionalFieldOf("offset", new Vector2i()).forGetter(AnchoredPosBlueprint::offset),
        AnchoredPos.Anchor.CODEC.fieldOf("anchor").forGetter(AnchoredPosBlueprint::anchor)
    ).apply(instance, AnchoredPosBlueprint::new));

    @Override
    public PositionType<?> getType()
    {
        return PositionType.ANCHORED;
    }

    @Override
    public List<?> createComponents()
    {
        return List.of(new AnchoredPos(new Vector2i(offset), anchor));
    }
}
