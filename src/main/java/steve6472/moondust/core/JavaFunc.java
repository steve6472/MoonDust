package steve6472.moondust.core;

import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;

import java.util.function.Function;

/**
 * Created by steve6472
 * Date: 6/21/2025
 * Project: MoonDust <br>
 */
public class JavaFunc implements Keyable
{
    private final Key key;
    private final Function<Object, Object> func;

    public JavaFunc(Key key, Function<Object, Object> func)
    {
        this.key = key;
        this.func = func;
    }

    public Object runFunction(Object input)
    {
        return func.apply(input);
    }

    @Override
    public Key key()
    {
        return key;
    }
}
