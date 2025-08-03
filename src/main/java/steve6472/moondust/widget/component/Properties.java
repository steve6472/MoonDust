package steve6472.moondust.widget.component;

import steve6472.moondust.core.Mergeable;
import steve6472.moondust.view.property.Property;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.event.OnPropertyChange;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * Created by steve6472
 * Date: 8/3/2025
 * Project: MoonDust <br>
 */
public final class Properties implements Mergeable<Properties>
{
    private final Map<String, Property<?>> properties;
    private final Set<String> widgetBound = new HashSet<>();

    public Properties(Map<String, Property<?>> properties)
    {
        this.properties = properties;
    }

    // Forbids overriding properties
    @Override
    public Properties merge(Properties left, Properties right)
    {
        int totalSize = left.properties().size() + right.properties.size();
        Map<String, Property<?>> propertyMap = new HashMap<>(totalSize);
        propertyMap.putAll(left.properties());
        right.properties().forEach((k, v) ->
        {
            Property<?> put = propertyMap.put(k, v);
            if (put != null)
                throw new RuntimeException("Property '%s' already exists".formatted(k));
        });

        Properties prop = new Properties(propertyMap);
        prop.widgetBound.addAll(left.widgetBound);
        prop.widgetBound.addAll(right.widgetBound);
        return prop;
    }

    public void bindToWidget(Widget widget)
    {
        properties.forEach((key, property) -> {
            if (widgetBound.contains(key))
                return;
            widgetBound.add(key);
            property.addListener((_, oldVal, newVal) -> {
                widget.handleEvents(OnPropertyChange.class, _ -> true, new OnPropertyChange(key, oldVal, newVal));
            });
        });
    }

    public Map<String, Property<?>> properties()
    {
        return properties;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (Properties) obj;
        return Objects.equals(this.properties, that.properties);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(properties);
    }

    @Override
    public String toString()
    {
        return "Properties{" + "properties=" + properties + ", widgetBound=" + widgetBound + '}';
    }
}
