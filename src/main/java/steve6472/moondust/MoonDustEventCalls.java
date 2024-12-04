package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.moondust.widget.blueprint.event.UIEvent;
import steve6472.moondust.widget.blueprint.event.impl.RandomTick;
import steve6472.moondust.core.EventCall;
import steve6472.moondust.core.EventCallWrapper;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public interface MoonDustEventCalls
{
    interface Button
    {
        EventCallWrapper<RandomTick> RANDOM_TEST = wrap(key("button/random_test"), (comp, event) -> {

        });
    }

    private static <T extends UIEvent> EventCallWrapper<T> wrap(Key key, EventCall<T> eventCall)
    {
        EventCallWrapper<T> wrapper = new EventCallWrapper<>(key, eventCall);
        MoonDustRegistries.EVENT_CALLS.register(wrapper);
        return wrapper;
    }

    private static Key key(String id)
    {
        return Key.withNamespace(MoonDustConstants.NAMESPACE, id);
    }
}
