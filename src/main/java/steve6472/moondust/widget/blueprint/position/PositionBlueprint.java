package steve6472.moondust.widget.blueprint.position;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Typed;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public interface PositionBlueprint extends Typed<PositionBlueprintType<?>>, Blueprint
{
    Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "position");
    Codec<PositionBlueprint> CODEC = Codec.withAlternative(MoonDustRegistries.POSITION_BLUEPRINT_TYPE.byKeyCodec().dispatch("type", PositionBlueprint::getType, PositionBlueprintType::mapCodec), AbsolutePosBlueprint.CODEC_SMALL);
}
