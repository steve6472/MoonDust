package steve6472.moondust.widget.component.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector2i;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.blueprint.position.RelativePosBlueprint;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record RelativePos(Vector2i offset, String parent, boolean isRight) implements Position
{
    public static final Codec<RelativePos> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.VEC_2I.optionalFieldOf("offset", new Vector2i()).forGetter(RelativePos::offset),
        Codec.STRING.fieldOf("parent").forGetter(RelativePos::parent),
        Codec.BOOL.optionalFieldOf("is_right", false).forGetter(RelativePos::isRight)
    ).apply(instance, RelativePos::new));

    @Override
    public void evaluatePosition(Vector2i store, Widget widget)
    {
        widget.parent().ifPresent(parentWidget -> {
            parentWidget.getChild(parent).ifPresent(child -> {
                Vector2i position = child.getPosition();
                store.set(position).add(offset);
                if (isRight)
                    child.getBounds().ifPresent(s -> store.add(s.width(), 0));
            });
        });
    }

    @Override
    public PositionType<?> getType()
    {
        return PositionType.RELATIVE;
    }
}
