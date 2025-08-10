package steve6472.moondust.view.property;

import steve6472.core.setting.StringSetting;
import steve6472.moondust.view.exp.Expression;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public class StringProperty extends Property<String>
{
    public StringProperty()
    {
        this("");
    }

    public StringProperty(String value)
    {
        set(value);
    }

    public static StringProperty fromSetting(StringSetting setting)
    {
        StringProperty property = new StringProperty(setting.get());
        property.addListener((_, _, nVal) -> setting.set(nVal));
        return property;
    }

    public Expression<String> copyFrom()
    {
        return new Expression<>(this::get, this);
    }
}
