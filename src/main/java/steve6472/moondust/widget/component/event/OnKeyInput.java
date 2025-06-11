package steve6472.moondust.widget.component.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Created by steve6472
 * Date: 2/13/2025
 * Project: MoonDust <br>
 */
public record OnKeyInput(int key, int scancode, int action, int mods) implements UIEvent
{
    public static final OnKeyInput INSTANCE = new OnKeyInput(-42, -69, -420, -7);

    public static final Codec<OnKeyInput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("key").forGetter(OnKeyInput::key),
        Codec.INT.fieldOf("scancode").forGetter(OnKeyInput::scancode),
        Codec.INT.fieldOf("action").forGetter(OnKeyInput::action),
        Codec.INT.fieldOf("mods").forGetter(OnKeyInput::mods)
    ).apply(instance, OnKeyInput::new));
}
