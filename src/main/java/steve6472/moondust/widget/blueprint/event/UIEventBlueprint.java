package steve6472.moondust.widget.blueprint.event;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Typed;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public interface UIEventBlueprint extends Typed<UIEventType<?>>, Blueprint
{
    Codec<UIEventBlueprint> CODEC = MoonDustRegistries.EVENT_TYPE.byKeyCodec().dispatch("type", UIEventBlueprint::getType, UIEventType::mapCodec);
}
