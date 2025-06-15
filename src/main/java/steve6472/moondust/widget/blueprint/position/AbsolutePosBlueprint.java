package steve6472.moondust.widget.blueprint.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector2i;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.widget.component.position.AbsolutePos;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 * Absolute position within the parent widget
 */
public record AbsolutePosBlueprint(Vector2i position) implements PositionBlueprint
{
    public static final Codec<AbsolutePosBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.VEC_2I.fieldOf("pos").forGetter(AbsolutePosBlueprint::position)
    ).apply(instance, AbsolutePosBlueprint::new));

    @Override
    public PositionBlueprintType<?> getType()
    {
        return PositionBlueprintType.ABSOLUTE;
    }

    @Override
    public List<?> createComponents()
    {
        return List.of(new AbsolutePos(new Vector2i(position)));
    }
}
