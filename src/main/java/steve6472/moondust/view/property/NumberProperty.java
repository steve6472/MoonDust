package steve6472.moondust.view.property;

import steve6472.core.setting.FloatSetting;
import steve6472.moondust.view.exp.Compare;
import steve6472.moondust.view.exp.Expression;
import steve6472.moondust.view.exp.Const;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public class NumberProperty extends Property<Double>
{
    public NumberProperty()
    {
        this(0);
    }

    public NumberProperty(double value)
    {
        set(value);
    }

    public static NumberProperty fromSetting(FloatSetting setting)
    {
        NumberProperty property = new NumberProperty(setting.get());
        property.addListener((_, _, nVal) -> setting.set(nVal.floatValue()));
        return property;
    }

    public Expression<Double> copyFrom()
    {
        return new Expression<>(this::get, this);
    }

    public Expression<Boolean> greaterThan(double greaterThan)
    {
        return new Expression<>(Compare.Number.greaterThan(this, Const.constDouble(greaterThan)), this);
    }
}
