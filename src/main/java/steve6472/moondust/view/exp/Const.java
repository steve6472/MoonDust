package steve6472.moondust.view.exp;

import steve6472.moondust.view.property.Property;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
@SuppressWarnings("unused")
public interface Const
{
    ConstantProperty<Boolean> TRUE = new ConstantProperty<>(true);
    ConstantProperty<Boolean> FALSE = new ConstantProperty<>(false);

    static Property<Integer> constInt(int constant)
    {
        return new ConstantProperty<>(constant);
    }

    static Property<Double> constDouble(double constant)
    {
        return new ConstantProperty<>(constant);
    }

    static Property<Boolean> constBool(boolean constant)
    {
        return constant ? TRUE : FALSE;
    }

    static Property<String> constString(String constant)
    {
        return new ConstantProperty<>(constant);
    }
}
