package steve6472.moondust.view.property;

import steve6472.moondust.view.exp.Compare;
import steve6472.moondust.view.exp.Expression;
import steve6472.moondust.view.exp.Const;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public class IntegerProperty extends Property<Integer>
{
    public IntegerProperty()
    {
        this(0);
    }

    public IntegerProperty(int value)
    {
        set(value);
    }

    public Expression<Boolean> greaterThan(int right)
    {
        return new Expression<>(Compare.Int.greaterThan(this, Const.constInt(right)), this);
    }

    public Expression<Boolean> greaterThan(Property<Integer> right)
    {
        return new Expression<>(Compare.Int.greaterThan(this, right), this, right);
    }
}
