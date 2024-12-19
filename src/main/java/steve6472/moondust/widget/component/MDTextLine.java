package steve6472.moondust.widget.component;

import org.joml.Vector2i;
import steve6472.flare.ui.font.render.UITextLine;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record MDTextLine(UITextLine line, Vector2i offset)
{
    public MDTextLine replaceText(String newText)
    {
        return new MDTextLine(new UITextLine(newText, line.size(), line.style(), line.anchor()), new Vector2i(offset));
    }
}
