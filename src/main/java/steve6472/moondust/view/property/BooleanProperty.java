package steve6472.moondust.view.property;

import steve6472.core.setting.BoolSetting;
import steve6472.moondust.view.exp.Expression;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public class BooleanProperty extends Property<Boolean>
{
    public BooleanProperty()
    {
        this(false);
    }

    public BooleanProperty(boolean value)
    {
        set(value);
    }

    public static BooleanProperty fromSetting(BoolSetting setting)
    {
        BooleanProperty property = new BooleanProperty(setting.get());
        property.addListener((_, _, nVal) -> setting.set(nVal));
        return property;
    }

    public Expression<Boolean> copyFrom()
    {
        return new Expression<>(this::get, this);
    }
}
