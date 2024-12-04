package steve6472.moondust.child.component.position;

import org.joml.Vector2i;
import steve6472.moondust.widget.Widget;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public interface Position
{
    void evaluatePosition(Vector2i store, Widget widget);
}
