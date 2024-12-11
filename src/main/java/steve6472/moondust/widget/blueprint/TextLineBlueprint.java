package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector2i;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;
import steve6472.flare.ui.font.render.TextLine;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.UITextLine;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record TextLineBlueprint(TextLine line, Vector2i offset) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "text_line");
    public static final Codec<TextLineBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TextLine.CODEC.fieldOf("text").forGetter(TextLineBlueprint::line),
        ExtraCodecs.VEC_2I.optionalFieldOf("offset", new Vector2i()).forGetter(TextLineBlueprint::offset)
    ).apply(instance, TextLineBlueprint::new));

    @Override
    public List<?> createComponents()
    {
        return List.of(new UITextLine(line, offset));
    }
}
