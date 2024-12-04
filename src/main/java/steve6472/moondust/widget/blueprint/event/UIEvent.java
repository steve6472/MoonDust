package steve6472.moondust.widget.blueprint.event;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Typed;
import steve6472.moondust.MoonDustRegistries;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public interface UIEvent extends Typed<UIEventType<?>>
{
    Codec<UIEvent> CODEC = MoonDustRegistries.EVENT_TYPE.byKeyCodec().dispatch("type", UIEvent::getType, UIEventType::mapCodec);
}
