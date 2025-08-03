package steve6472.moondust.view;

import steve6472.core.registry.Key;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public record Command(Key key, Object value)
{
    public Command(Key key)
    {
        this(key, null);
    }
}
