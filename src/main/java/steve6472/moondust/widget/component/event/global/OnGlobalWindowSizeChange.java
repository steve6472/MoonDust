package steve6472.moondust.widget.component.event.global;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.widget.component.Bounds;
import steve6472.moondust.widget.component.event.UIEvent;

/**
 * Created by steve6472
 * Date: 5/21/2025
 * Project: MoonDust <br>
 */
public record OnGlobalWindowSizeChange(int width, int height) implements UIEvent
{
    public static final Codec<OnGlobalWindowSizeChange> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("width").forGetter(o -> o.width),
        Codec.INT.fieldOf("height").forGetter(o -> o.height)
    ).apply(instance, OnGlobalWindowSizeChange::new));
}
