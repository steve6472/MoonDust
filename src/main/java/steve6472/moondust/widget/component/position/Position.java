package steve6472.moondust.widget.component.position;

import com.mojang.serialization.Codec;
import org.joml.Vector2i;
import steve6472.core.registry.Typed;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.blueprint.position.PositionBlueprint;
import steve6472.moondust.widget.blueprint.position.PositionBlueprintType;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public interface Position extends Typed<PositionType<?>>
{
    Codec<Position> CODEC = Codec.withAlternative(MoonDustRegistries.POSITION_TYPE.byKeyCodec().dispatch("type", Position::getType, PositionType::mapCodec), AbsolutePos.CODEC_SMALL);
    void evaluatePosition(Vector2i store, Widget widget);
}
