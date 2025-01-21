package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.moondust.widget.blueprint.event.UIEventBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.component.event.OnDataChange;

import java.util.List;

/**
 * Created by steve6472
 * Date: 1/21/2025
 * Project: MoonDust <br>
 */
public record OnDataChangeBlueprint(List<Key> floats, List<Key> ints, List<Key> strings, List<Key> flags) implements UIEventBlueprint
{
    public static final Codec<OnDataChangeBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Key.CODEC.listOf().optionalFieldOf("floats", List.of()).forGetter(OnDataChangeBlueprint::floats),
        Key.CODEC.listOf().optionalFieldOf("ints", List.of()).forGetter(OnDataChangeBlueprint::ints),
        Key.CODEC.listOf().optionalFieldOf("strings", List.of()).forGetter(OnDataChangeBlueprint::strings),
        Key.CODEC.listOf().optionalFieldOf("flags", List.of()).forGetter(OnDataChangeBlueprint::flags)
    ).apply(instance, OnDataChangeBlueprint::new));

    @Override
    public UIEventType<?> getType()
    {
        return UIEventType.ON_DATA_CHANGE;
    }

    @Override
    public List<?> createComponents()
    {
        return List.of(new OnDataChange(floats, ints, strings, flags));
    }
}
