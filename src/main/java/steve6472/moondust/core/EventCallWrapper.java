package steve6472.moondust.core;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;
import steve6472.core.registry.Serializable;
import steve6472.moondust.widget.blueprint.event.UIEvent;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record EventCallWrapper<T extends UIEvent>(Key key, EventCall<T> eventCall) implements Keyable, Serializable<EventCallWrapper>
{

    @Override
    public Key key()
    {
        return key;
    }

    @Override
    public Codec<EventCallWrapper> codec()
    {
        return null;
    }
}
