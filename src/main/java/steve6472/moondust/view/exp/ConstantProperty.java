package steve6472.moondust.view.exp;

import steve6472.moondust.view.ChangeListener;
import steve6472.moondust.view.property.Property;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public class ConstantProperty<T> extends Property<T>
{
    private boolean isSet = false;

    public ConstantProperty(T value)
    {
        set(value);
        isSet = true;
    }

    @Override
    public void set(T newValue)
    {
        if (isSet)
            throw new IllegalStateException("Can not change constant");
        super.set(newValue);
    }

    @Override
    public void bind(Expression<T> binder)
    {
        throw new IllegalStateException("Probalby dont");
    }

    @Override
    public void addListener(ChangeListener<T> listener)
    {
        throw new IllegalStateException("Constant value can not change, listener is pointless");
    }
}
