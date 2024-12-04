package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import steve6472.core.registry.StringValue;

import java.util.Locale;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public enum Visible implements StringValue
{
    YES,
    NO;

    public static final Codec<Visible> CODEC = StringValue.fromValues(Visible::values);

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
