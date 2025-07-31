package steve6472.moondust.widget.component.event.global;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.widget.component.event.UIEvent;

/**
 * Created by steve6472
 * Date: 5/21/2025
 * Project: MoonDust <br>
 */
public record OnGlobalPixelScaleChange(int scale) implements UIEvent
{
    public static final Codec<OnGlobalPixelScaleChange> CODEC = Codec.INT.xmap(OnGlobalPixelScaleChange::new, OnGlobalPixelScaleChange::scale);
}
