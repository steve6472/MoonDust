package steve6472.moondust.widget.component.position;

import org.joml.Vector2i;
import steve6472.moondust.widget.Widget;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record RelativePos(Vector2i offset, String parent) implements Position
{
    @Override
    public void evaluatePosition(Vector2i store, Widget widget)
    {
        widget.parent().ifPresent(parentWidget -> {
            parentWidget.getChild(parent).ifPresent(child -> {
                Vector2i position = child.getPosition();
                store.set(position).add(offset);
            });
        });
    }
}
