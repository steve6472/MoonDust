package steve6472.moondust.view;

import steve6472.moondust.view.property.Property;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
@FunctionalInterface
public interface ChangeListener<T>
{
    void changed(Property<T> property, T oldValue, T newValue);
}
