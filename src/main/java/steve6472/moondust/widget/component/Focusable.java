package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import steve6472.core.registry.StringValue;

import java.util.Locale;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public enum Focusable implements StringValue
{
    YES,
    NO;

    public static final Codec<Focusable> CODEC = StringValue.fromValues(Focusable::values);

    public boolean flag()
    {
        return this == YES;
    }

    @Override
    public String stringValue()
    {
        return name().toLowerCase(Locale.ROOT);
    }
}
