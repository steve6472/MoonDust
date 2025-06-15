package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
    public static final Codec<MDText> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Text.CODEC.fieldOf("text").forGetter(MDText::text),
        Position.CODEC.fieldOf("position").forGetter(MDText::position)
    ).apply(instance, MDText::new));

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
