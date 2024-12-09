package steve6472.moondust.widget.blueprint.event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.event.UIEventCallEntry;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record UIEventCallBlueprint(Key call, UIEventBlueprint event) implements Blueprint
{
    public static final Codec<UIEventCallBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Key.CODEC.fieldOf("call").forGetter(UIEventCallBlueprint::call),
        UIEventBlueprint.CODEC.fieldOf("event").forGetter(UIEventCallBlueprint::event)
    ).apply(instance, UIEventCallBlueprint::new));

    @Override
    public List<?> createComponents()
    {
        List<?> components = event.createComponents();
        if (components.size() != 1)
            throw new RuntimeException("Event blueprint returned incorrect amount of components (" + components.size() + ") " + event.getType().key());
        return List.of(new UIEventCallEntry(call, components.getFirst()));
    }
}
