package steve6472.moondust.widget.component.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.MoonDust;

/**
 * Created by steve6472
 * Date: 2/13/2025
 * Project: MoonDust <br>
 */
public record OnPropertyChange(String property, Object oldValue, Object newValue) implements UIEvent
{
    public static final Codec<OnPropertyChange> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("property").forGetter(OnPropertyChange::property),
        MoonDust.CODEC_LUA_VALUE.fieldOf("old_value").forGetter(OnPropertyChange::oldValue),
        MoonDust.CODEC_LUA_VALUE.fieldOf("new_value").forGetter(OnPropertyChange::newValue)
    ).apply(instance, OnPropertyChange::new));
}
