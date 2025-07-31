package steve6472.moondust.view.property;

import steve6472.moondust.view.ChangeListener;
import steve6472.moondust.view.exp.Exp;
import steve6472.moondust.view.exp.Expression;
import steve6472.moondust.view.exp.ConstantProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public abstract class Property<T>
{
    private final List<ChangeListener<T>> changeListeners = new ArrayList<>();
    private String debugName = UUID.randomUUID().toString();

    private Expression<T> binder;
    private T value;

    public void addListener(ChangeListener<T> listener)
    {
        this.changeListeners.add(listener);
    }

    public void callChangeListeners(T oldValue, T newValue)
    {
        if (Objects.equals(oldValue, newValue))
            return;

        changeListeners.forEach(l -> l.changed(this, oldValue, newValue));
    }

    public T get()
    {
        return value;
    }

    public void set(T newValue)
    {
        if (isBound())
            throw new IllegalStateException("A bound property can not be set");
        setInternal(newValue);
    }

    protected void setInternal(T newValue)
    {
        T oldValue = value;
        value = newValue;
        callChangeListeners(oldValue, newValue);
    }

    public void setDebugName(String debugName)
    {
        this.debugName = debugName;
    }

    public String getDebugName()
    {
        return debugName;
    }

    public boolean isBound()
    {
        return binder != null;
    }

    public Expression<T> getBinder()
    {
        return binder;
    }

    public void bind(Exp<T> exp, Property<?>... dependencies)
    {
        bind(new Expression<>(exp, dependencies));
    }

    public void bind(Expression<T> binder)
    {
        if (isBound())
            throw new IllegalStateException("Property can not be rebound");

        this.binder = binder;
        Property<?>[] dependencies = this.binder.getDependencies();
        for (Property<?> dependency : dependencies)
        {
            if (dependency instanceof ConstantProperty<?>)
                continue;

            dependency.addListener((_, _, _) ->
            {
                T calculated = binder.getExp().calculate();
                setInternal(calculated);
            });
        }
    }
}