package steve6472.moondust.widget.component.position;

import com.mojang.serialization.Codec;
import org.joml.Vector2i;
import steve6472.core.registry.StringValue;
import steve6472.moondust.widget.component.Bounds;
import steve6472.moondust.widget.Widget;

import java.util.Locale;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record AnchoredPos(Vector2i offset, Anchor anchor) implements Position
{
    @Override
    public void evaluatePosition(Vector2i store, Widget widget)
    {
        widget.parent().ifPresentOrElse(parent ->
        {
            parent.getComponent(Bounds.class).ifPresent(parentBounds -> {
                widget.getComponent(Bounds.class).ifPresent(bounds -> {
                    Vector2i parentPos = parent.getPosition();
                    anchor.setPos(store, offset, parentPos, parentBounds, bounds);
                });
            });
        }, () -> store.set(offset));
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

        public void setPos(Vector2i store, Vector2i offset, Vector2i parentPos, Bounds parentBounds, Bounds bounds)
        {
            store.set(parentPos);
            switch (this) {
                case CENTER -> {
                    int x = parentBounds.width / 2 - bounds.width / 2 + offset.x;
                    int y = parentBounds.height / 2 - bounds.height / 2 + offset.y;
                    store.add(x, y);
                }
                case TOP -> {
                    int x = parentBounds.width / 2 - bounds.width / 2 + offset.x;
                    int y = offset.y;
                    store.add(x, y);
                }
                case LEFT -> {
                    int x = offset.x;
                    int y = parentBounds.height / 2 - bounds.height / 2 + offset.y;
                    store.add(x, y);
                }
                case BOTTOM -> {
                    int x = parentBounds.width / 2 - bounds.width / 2 + offset.x;
                    int y = parentBounds.height - bounds.height + offset.y;
                    store.add(x, y);
                }
                case RIGHT -> {
                    int x = parentBounds.width - bounds.width + offset.x;
                    int y = parentBounds.height / 2 - bounds.height / 2 + offset.y;
                    store.add(x, y);
                }
                case TOP_LEFT -> {
                    int x = offset.x;
                    int y = offset.y;
                    store.add(x, y);
                }
                case BOTTOM_LEFT -> {
                    int x = offset.x;
                    int y = parentBounds.height - bounds.height + offset.y;
                    store.add(x, y);
                }
                case TOP_RIGHT -> {
                    int x = parentBounds.width - bounds.width + offset.x;
                    int y = offset.y;
                    store.add(x, y);
                }
                case BOTTOM_RIGHT -> {
                    int x = parentBounds.width - bounds.width + offset.x;
                    int y = parentBounds.height - bounds.height + offset.y;
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
