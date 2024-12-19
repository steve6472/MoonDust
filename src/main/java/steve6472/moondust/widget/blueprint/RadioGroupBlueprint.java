package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.RadioGroup;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/18/2024
 * Project: MoonDust <br>
 */
public record RadioGroupBlueprint(String groupKey, boolean defaultSelected) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "radio_group");
    public static final Codec<RadioGroupBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("group").forGetter(RadioGroupBlueprint::groupKey),
        Codec.BOOL.optionalFieldOf("selected", false).forGetter(RadioGroupBlueprint::defaultSelected)
    ).apply(instance, RadioGroupBlueprint::new));

    @Override
    public List<?> createComponents()
    {
        RadioGroup group = new RadioGroup();
        group.groupKey = groupKey;
        group.selected = defaultSelected;
        return List.of(group);
    }
}
