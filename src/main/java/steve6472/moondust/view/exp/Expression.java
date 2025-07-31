package steve6472.moondust.view.exp;

import steve6472.moondust.view.property.Property;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public class Expression<T>
{
    private final Property<?>[] dependencies;
    private final Exp<T> exp;

    public Expression(Exp<T> exp, Property<?>... dependencies)
    {
        this.dependencies = dependencies;
        this.exp = exp;
    }

    public Property<?>[] getDependencies()
    {
        return dependencies;
    }

    public Exp<T> getExp()
    {
        return exp;
    }
}
