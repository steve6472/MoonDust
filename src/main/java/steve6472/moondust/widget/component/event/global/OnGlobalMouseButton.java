package steve6472.moondust.widget.component.event.global;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.widget.component.event.UIEvent;

/**
 * Created by steve6472
 * Date: 5/21/2025
 * Project: MoonDust <br>
 */
public record OnGlobalMouseButton(int button, int action, int mods) implements UIEvent
{
    public static final Codec<OnGlobalMouseButton> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("button").forGetter(OnGlobalMouseButton::button),
        Codec.INT.fieldOf("action").forGetter(OnGlobalMouseButton::action),
        Codec.INT.fieldOf("mods").forGetter(OnGlobalMouseButton::mods)
    ).apply(instance, OnGlobalMouseButton::new));
}
