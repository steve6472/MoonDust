package steve6472.moondust.widget.blueprint.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record UIEventCall(Key call, UIEvent event)
{
    public static final Codec<UIEventCall> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Key.CODEC.fieldOf("call").forGetter(UIEventCall::call),
        UIEvent.CODEC.fieldOf("event").forGetter(UIEventCall::event)
    ).apply(instance, UIEventCall::new));
}
