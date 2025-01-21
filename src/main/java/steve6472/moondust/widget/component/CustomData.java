package steve6472.moondust.widget.component;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import org.jetbrains.annotations.ApiStatus;
import steve6472.core.registry.Key;
import steve6472.moondust.core.Mergeable;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.event.OnDataChange;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class CustomData implements Mergeable<CustomData>
{
    public final Object2FloatArrayMap<Key> floats = new Object2FloatArrayMap<>();
    public final Object2IntArrayMap<Key> ints = new Object2IntArrayMap<>();
    public final Map<Key, String> strings = new HashMap<>();
    public final Object2BooleanArrayMap<Key> flags = new Object2BooleanArrayMap<>();

    @ApiStatus.Internal
    public Widget widget;

    public CustomData()
    {
        floats.defaultReturnValue(0f);
        ints.defaultReturnValue(0);
        flags.defaultReturnValue(false);
    }

    public void putFloat(Key key, float value)
    {
        if (floats.put(key, value) != value)
        {
            if (widget != null)
                widget.handleEvents(OnDataChange.class, event -> event.floats().contains(key));
        }
    }

    public void putInt(Key key, int value)
    {
        if (ints.put(key, value) != value)
        {
            if (widget != null)
                widget.handleEvents(OnDataChange.class, event -> event.ints().contains(key));
        }
    }

    public void putString(Key key, String value)
    {
        if (!value.equals(strings.put(key, value)))
        {
            if (widget != null)
                widget.handleEvents(OnDataChange.class, event -> event.strings().contains(key));
        }
    }

    public void putFlag(Key key, boolean value)
    {
        if (flags.put(key, value) != value)
        {
            if (widget != null)
                widget.handleEvents(OnDataChange.class, event -> event.flags().contains(key));
        }
    }

    public float getFloat(Key key)
    {
        return floats.getFloat(key);
    }

    public int getInt(Key key)
    {
        return ints.getInt(key);
    }

    public String getString(Key key)
    {
        return strings.get(key);
    }

    public boolean getFlag(Key key)
    {
        return flags.getBoolean(key);
    }

    @Override
    public CustomData merge(CustomData left, CustomData right)
    {
        CustomData data = new CustomData();
        data.floats.putAll(left.floats);
        data.floats.putAll(right.floats);
        data.ints.putAll(left.ints);
        data.ints.putAll(right.ints);
        data.strings.putAll(left.strings);
        data.strings.putAll(right.strings);
        data.flags.putAll(left.flags);
        data.flags.putAll(right.flags);
        return data;
    }

    @Override
    public String toString()
    {
        return "CustomData{" + "floats=" + floats + ", ints=" + ints + ", strings=" + strings + ", flags=" + flags + '}';
    }
}
