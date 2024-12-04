package steve6472.moondust.widget.blueprint.event;

import com.mojang.serialization.Codec;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record Events(List<UIEventCall> events)
{
    public static final Codec<Events> CODEC = UIEventCall.CODEC.listOf().xmap(Events::new, Events::events);
}
