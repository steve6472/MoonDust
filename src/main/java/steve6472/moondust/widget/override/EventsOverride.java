package steve6472.moondust.widget.override;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.widget.BlueprintOverride;
import steve6472.moondust.widget.blueprint.event.EventsBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventCallBlueprint;
import steve6472.moondust.widget.component.event.UIEventCallEntry;
import steve6472.moondust.widget.component.event.UIEvents;

import java.util.*;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public record EventsOverride(List<Integer> remove, List<UIEventCallBlueprint> add) implements BlueprintOverride<UIEvents>
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "events");
    public static final Codec<EventsOverride> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.listOf().optionalFieldOf("remove", List.of()).forGetter(EventsOverride::remove),
        UIEventCallBlueprint.CODEC.listOf().optionalFieldOf("add", List.of()).forGetter(EventsOverride::add)
    ).apply(instance, EventsOverride::new));

    @Override
    public UIEvents override(UIEvents source)
    {
        List<UIEventCallEntry> events = new ArrayList<>(source.events());
        List<Integer> removeSorted = remove.stream().sorted(Comparator.reverseOrder()).toList();
        for (int i : removeSorted)
            events.remove(i);

        for (UIEventCallBlueprint uiEventCallBlueprint : add)
        {
            for (Object component : uiEventCallBlueprint.createComponents())
            {
                if (component instanceof UIEventCallEntry entry)
                    events.add(entry);
                else
                    throw new RuntimeException("Wsuierghsdber uih");
            }
        }

        return new UIEvents(events);
    }

    @Override
    public Class<UIEvents> target()
    {
        return UIEvents.class;
    }
}
