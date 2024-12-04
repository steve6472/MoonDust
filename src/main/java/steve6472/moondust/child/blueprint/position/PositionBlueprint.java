package steve6472.moondust.child.blueprint.position;

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
public interface PositionBlueprint extends Typed<PositionType<?>>, Blueprint
{
    Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "position");
    Codec<PositionBlueprint> CODEC = MoonDustRegistries.POSITION_TYPE.byKeyCodec().dispatch("type", PositionBlueprint::getType, PositionType::mapCodec);
}
