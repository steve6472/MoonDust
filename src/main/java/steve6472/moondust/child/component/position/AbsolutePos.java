package steve6472.moondust.child.component.position;

import org.joml.Vector2i;
import steve6472.moondust.widget.Widget;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 * Absolute position within the parent widget
 */
public record AbsolutePos(Vector2i position) implements Position
{
    @Override
    public void evaluatePosition(Vector2i store, Widget widget)
    {
        widget.parent().ifPresentOrElse(parent ->
        {
            Vector2i parentPos = parent.getPosition();
            store.set(parentPos).add(position);
        }, () -> store.set(position));
    }
}
