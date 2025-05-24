package steve6472.moondust.widget.component.event.global;

import com.mojang.serialization.Codec;
import steve6472.moondust.widget.component.event.UIEvent;

/**
 * Created by steve6472
 * Date: 5/21/2025
 * Project: MoonDust <br>
 */
public record OnGlobalScroll(double yOffset) implements UIEvent
{
    public static final Codec<OnGlobalScroll> CODEC = Codec.DOUBLE.xmap(OnGlobalScroll::new, OnGlobalScroll::yOffset);
}
