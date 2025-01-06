package steve6472.moondust.widget.component;

import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import steve6472.core.registry.Key;
import steve6472.moondust.core.Mergeable;

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

    public CustomData()
    {
        ints.defaultReturnValue(0);
    }

    public void putFloat(Key key, float value)
    {
        floats.put(key, value);
    }

    public void putInt(Key key, int value)
    {
        ints.put(key, value);
    }

    public void putString(Key key, String value)
    {
        strings.put(key, value);
    }

    /// Internally stores an int <br>
    /// if `value` = `true`, stores 1 <br>
    /// if `value` = `false`, removes the key (effectively storing a 0)
    public void putFlag(Key key, boolean value)
    {
        if (!value)
            ints.removeInt(key);
        else
            ints.put(key, 1);
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
        return ints.getInt(key) == 1;
    }

    @Override
    public String toString()
    {
        return "CustomData{" + "floats=" + floats + ", ints=" + ints + ", strings=" + strings + '}';
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
        return data;
    }
}
