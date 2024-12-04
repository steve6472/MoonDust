package steve6472.moondust.child.component.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector2i;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.child.blueprint.position.PositionBlueprint;
import steve6472.moondust.child.blueprint.position.PositionType;
import steve6472.moondust.widget.Widget;

import java.util.List;

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

    @Override
    public void evaluatePosition(Vector2i store, Widget widget)
    {
        widget.parent().ifPresentOrElse(parent ->
        {
            Position parentPosition = parent.getComponent(Position.class).orElseThrow();
            Vector2i parentPos = new Vector2i();
            parentPosition.evaluatePosition(parentPos, parent);
            store.set(parentPos).add(position);
        }, () -> store.set(position));
    }
}
