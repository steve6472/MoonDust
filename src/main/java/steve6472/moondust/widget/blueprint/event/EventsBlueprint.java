package steve6472.moondust.widget.blueprint.event;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.event.UIEventCallEntry;
import steve6472.moondust.widget.component.event.UIEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record EventsBlueprint(List<UIEventCallBlueprint> events) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "events");
    public static final Codec<EventsBlueprint> CODEC = UIEventCallBlueprint.CODEC.listOf().xmap(EventsBlueprint::new, EventsBlueprint::events);

    @Override
    public List<?> createComponents()
    {
        List<UIEventCallEntry> list = new ArrayList<>();
        for (UIEventCallBlueprint event : events)
        {
            List<?> components = event.createComponents();
            for (Object component : components)
            {
                if (component instanceof UIEventCallEntry call)
                    list.add(call);
                else
                    throw new IllegalStateException("Non-UIEventCall was returned by blueprint");
            }
        }
        return List.of(new UIEvents(list));
    }
}
