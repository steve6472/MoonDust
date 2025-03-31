package steve6472.moondust.widget.component;

import org.joml.Vector2i;
import steve6472.flare.ui.font.render.Text;
import steve6472.flare.ui.font.render.TextPart;
import steve6472.moondust.widget.component.position.Position;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record MDText(Text text, Position position)
{
    public void replaceText(TextPart part, int index)
    {
        text.parts().set(index, part);
    }

    public void replaceText(String message, int index)
    {
        TextPart element = text.parts().get(index);
        text.parts().set(index, new TextPart(message, element.size(), element.style()));
    }
}
