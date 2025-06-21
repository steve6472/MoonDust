package steve6472.moondust.widget.component.event;

import com.mojang.serialization.Codec;

/**
 * Created by steve6472
 * Date: 2/13/2025
 * Project: MoonDust <br>
 */
public record OnCharInput(int codepoint) implements UIEvent
{
    public static final OnCharInput INSTANCE = new OnCharInput(-42);

    public static final Codec<OnCharInput> CODEC = Codec.INT.xmap(OnCharInput::new, OnCharInput::codepoint);
}
