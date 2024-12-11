package steve6472.moondust.widget.component;

import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import steve6472.core.registry.Key;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class CustomData
{
    public final Object2FloatArrayMap<Key> floats = new Object2FloatArrayMap<>();
    public final Object2IntArrayMap<Key> ints = new Object2IntArrayMap<>();
    public final Map<Key, String> strings = new HashMap<>();

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

    @Override
    public String toString()
    {
        return "CustomData{" + "floats=" + floats + ", ints=" + ints + ", strings=" + strings + '}';
    }
}
