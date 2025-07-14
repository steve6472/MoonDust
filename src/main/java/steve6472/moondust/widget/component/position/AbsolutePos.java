package steve6472.moondust.widget.component.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector2i;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.blueprint.position.AbsolutePosBlueprint;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 * Absolute position within the parent widget
 */
public record AbsolutePos(Vector2i position) implements Position
{
    public static final Codec<AbsolutePos> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.VEC_2I.fieldOf("pos").forGetter(AbsolutePos::position)
    ).apply(instance, AbsolutePos::new));

    public static final Codec<AbsolutePos> CODEC_SMALL = ExtraCodecs.VEC_2I.xmap(AbsolutePos::new, AbsolutePos::position);

    @Override
    public void evaluatePosition(Vector2i store, Widget widget)
    {
        widget.parent().ifPresentOrElse(parent ->
        {
            Vector2i parentPos = parent.getPosition();
            store.set(parentPos).add(position);
        }, () -> store.set(position));
    }

    @Override
    public PositionType<?> getType()
    {
        return PositionType.ABSOLUTE;
    }
}
