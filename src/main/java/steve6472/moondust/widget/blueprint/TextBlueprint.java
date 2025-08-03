package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector2i;
import steve6472.core.registry.Key;
import steve6472.flare.ui.font.render.Text;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.blueprint.position.AbsolutePosBlueprint;
import steve6472.moondust.widget.blueprint.position.PositionBlueprint;
import steve6472.moondust.widget.component.MDText;
import steve6472.moondust.widget.component.position.Position;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record TextBlueprint(Text text, PositionBlueprint position, boolean inheritWidth) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "text");
    public static final Codec<TextBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Text.CODEC.fieldOf("text").forGetter(TextBlueprint::text),
        PositionBlueprint.CODEC.optionalFieldOf("position", new AbsolutePosBlueprint(new Vector2i())).forGetter(TextBlueprint::position),
        Codec.BOOL.optionalFieldOf("inherit_width", false).forGetter(TextBlueprint::inheritWidth)
    ).apply(instance, TextBlueprint::new));

    @Override
    public List<?> createComponents()
    {
        return List.of(new MDText(text.withMutableParts(), (Position) position.createComponents().getFirst(), inheritWidth));
    }
}
