package steve6472.moondust.widget.component.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector2i;
import steve6472.core.registry.StringValue;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.Size;

import java.util.Locale;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record AnchoredPos(Vector2i offset, Anchor anchor) implements Position
{
    public static final Codec<AnchoredPos> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.VEC_2I.fieldOf("offset").forGetter(AnchoredPos::offset),
        Anchor.CODEC.fieldOf("anchor").forGetter(AnchoredPos::anchor)
    ).apply(instance, AnchoredPos::new));

    @Override
    public void evaluatePosition(Vector2i store, Widget widget)
    {
        widget.parent().ifPresentOrElse(parent ->
        {
            parent.getBounds().ifPresent(parentBounds -> {
                widget.getBounds().ifPresent(bounds -> {
                    Vector2i parentPos = parent.getPosition();
                    anchor.setPos(store, offset, parentPos, parentBounds, bounds);
                });
            });
        }, () -> store.set(offset));
    }

    @Override
    public PositionType<?> getType()
    {
        return PositionType.ANCHORED;
    }

    public enum Anchor implements StringValue
    {
        CENTER,
        TOP,
        LEFT,
        BOTTOM,
        RIGHT,
        TOP_LEFT,
        BOTTOM_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT
        ;

        public static final Codec<Anchor> CODEC = StringValue.fromValues(Anchor::values);

        public void setPos(Vector2i store, Vector2i offset, Vector2i parentPos, Size parentBounds, Size bounds)
        {
            store.set(parentPos);
            switch (this) {
                case CENTER -> {
                    int x = parentBounds.width() / 2 - bounds.width() / 2 + offset.x;
                    int y = parentBounds.height() / 2 - bounds.height() / 2 + offset.y;
                    store.add(x, y);
                }
                case TOP -> {
                    int x = parentBounds.width() / 2 - bounds.width() / 2 + offset.x;
                    int y = offset.y;
                    store.add(x, y);
                }
                case LEFT -> {
                    int x = offset.x;
                    int y = parentBounds.height() / 2 - bounds.height() / 2 + offset.y;
                    store.add(x, y);
                }
                case BOTTOM -> {
                    int x = parentBounds.width() / 2 - bounds.width() / 2 + offset.x;
                    int y = parentBounds.height() - bounds.height() + offset.y;
                    store.add(x, y);
                }
                case RIGHT -> {
                    int x = parentBounds.width() - bounds.width() + offset.x;
                    int y = parentBounds.height() / 2 - bounds.height() / 2 + offset.y;
                    store.add(x, y);
                }
                case TOP_LEFT -> {
                    int x = offset.x;
                    int y = offset.y;
                    store.add(x, y);
                }
                case BOTTOM_LEFT -> {
                    int x = offset.x;
                    int y = parentBounds.height() - bounds.height() + offset.y;
                    store.add(x, y);
                }
                case TOP_RIGHT -> {
                    int x = parentBounds.width() - bounds.width() + offset.x;
                    int y = offset.y;
                    store.add(x, y);
                }
                case BOTTOM_RIGHT -> {
                    int x = parentBounds.width() - bounds.width() + offset.x;
                    int y = parentBounds.height() - bounds.height() + offset.y;
                    store.add(x, y);
                }
            }
        }

        @Override
        public String stringValue()
        {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
