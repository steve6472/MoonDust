package steve6472.moondust.view.property;

import steve6472.moondust.view.ChangeListener;
import steve6472.moondust.view.DependencyChecker;
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

    // Used when clearing
    private final List<ChangeListener<T>> binderListeners = new ArrayList<>();
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
        if (dependencies == null || dependencies.length == 0)
            throw new RuntimeException("At least one dependency must exist!");
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

            //noinspection rawtypes
            ChangeListener changeListener = (_, _, _) ->
            {
                T calculated = binder.getExp().calculate();
                setInternal(calculated);
            };
            //noinspection unchecked
            binderListeners.add(changeListener);
            //noinspection unchecked
            dependency.addListener(changeListener);
        }

        DependencyChecker dependencyChecker = new DependencyChecker();
        dependencyChecker.check(this);

        setInternal(binder.getExp().calculate());
    }

    public void clear()
    {
        if (binder == null)
            return;

        for (Property<?> dependency : this.binder.getDependencies())
        {
            for (ChangeListener<T> binderListener : binderListeners)
            {
                dependency.changeListeners.remove(binderListener);
            }
        }
        binderListeners.clear();
        binder = null;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "{" + "debugName='" + debugName + '\'' + '}';
    }
}