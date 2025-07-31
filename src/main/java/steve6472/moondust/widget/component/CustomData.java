package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import org.jetbrains.annotations.ApiStatus;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.core.Mergeable;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.event.OnDataChange;
import steve6472.moondust.widget.component.event.OnDataChanged;
import steve6472.radiant.LuauTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class CustomData implements Mergeable<CustomData>
{
    public final Object2DoubleArrayMap<Key> doubles = new Object2DoubleArrayMap<>();
    public final Object2IntArrayMap<Key> ints = new Object2IntArrayMap<>();
    public final Map<Key, String> strings = new HashMap<>();
    public final Object2BooleanArrayMap<Key> flags = new Object2BooleanArrayMap<>();
    public final Map<Key, LuauTable> tables = new HashMap<>();

    public static final Codec<CustomData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.mapListCodec(Key.CODEC, Codec.STRING).optionalFieldOf("strings", new HashMap<>()).forGetter(c -> c.strings),
        ExtraCodecs.mapListCodec(Key.CODEC, Codec.DOUBLE).optionalFieldOf("nums", new HashMap<>()).forGetter(c -> c.doubles),
        ExtraCodecs.mapListCodec(Key.CODEC, Codec.INT).optionalFieldOf("ints", new HashMap<>()).forGetter(c -> c.ints),
        ExtraCodecs.mapListCodec(Key.CODEC, Codec.BOOL).optionalFieldOf("flags", new HashMap<>()).forGetter(c -> c.flags)
    ).apply(instance, (strs, dbls, nts, flgs) -> {
        CustomData data = new CustomData();
        data.strings.putAll(strs);
        data.doubles.putAll(dbls);
        data.ints.putAll(nts);
        data.flags.putAll(flgs);
        return data;
    }));

    /// Used to run data change events
    @ApiStatus.Internal
    public Widget widget;

    public CustomData()
    {
        doubles.defaultReturnValue(0.0);
        ints.defaultReturnValue(0);
        flags.defaultReturnValue(false);
    }

    public void putDouble(Key key, double value)
    {
        double previous = doubles.put(key, value);
        if (previous != value)
        {
            if (widget != null)
            {
                widget.handleEvents(OnDataChange.class, event -> event.doubles().contains(key));
                widget.handleEvents(OnDataChanged.class, _ -> true, new OnDataChanged.Num(key, previous, value, false));
            }
        }
    }

    public void putInt(Key key, int value)
    {
        int previous = ints.put(key, value);
        if (previous != value)
        {
            if (widget != null)
            {
                widget.handleEvents(OnDataChange.class, event -> event.ints().contains(key));
                widget.handleEvents(OnDataChanged.class, _ -> true, new OnDataChanged.Int(key, previous, value, false));
            }
        }
    }

    public void putString(Key key, String value)
    {
        String previous = strings.put(key, value);
        if (!value.equals(previous))
        {
            if (widget != null)
            {
                widget.handleEvents(OnDataChange.class, event -> event.strings().contains(key));
                widget.handleEvents(OnDataChanged.class, _ -> true, new OnDataChanged.String(key, previous, value, false));
            }
        }
    }

    public void putFlag(Key key, boolean value)
    {
        if (flags.put(key, value) != value)
        {
            if (widget != null)
            {
                widget.handleEvents(OnDataChange.class, event -> event.flags().contains(key));
                widget.handleEvents(OnDataChanged.class, _ -> true, new OnDataChanged.Flag(key, !value, value, false));
            }
        }
    }

    public void putTable(Key key, LuauTable value)
    {
        LuauTable previous = tables.put(key, value);
        if (!value.equals(previous))
        {
            if (widget != null)
            {
                widget.handleEvents(OnDataChanged.class, _ -> true, new OnDataChanged.Table(key, previous, value, false));
            }
        }
    }

    public double getDouble(Key key)
    {
        return doubles.getDouble(key);
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

    public LuauTable getTable(Key key)
    {
        return tables.get(key);
    }

    public double removeDouble(Key key)
    {
        double previous = doubles.removeDouble(key);
        if (widget != null)
            widget.handleEvents(OnDataChanged.class, _ -> true, new OnDataChanged.Num(key, previous, 0, true));
        return previous;
    }

    public int removeInt(Key key)
    {
        int previous = ints.removeInt(key);
        if (widget != null)
            widget.handleEvents(OnDataChanged.class, _ -> true, new OnDataChanged.Int(key, previous, 0, true));
        return previous;
    }

    public String removeString(Key key)
    {
        String previous = strings.remove(key);
        if (widget != null)
            widget.handleEvents(OnDataChanged.class, _ -> true, new OnDataChanged.String(key, previous, null, true));
        return previous;
    }

    public boolean removeFlag(Key key)
    {
        boolean previous = flags.removeBoolean(key);
        if (widget != null)
            widget.handleEvents(OnDataChanged.class, _ -> true, new OnDataChanged.Flag(key, previous, false, true));
        return previous;
    }

    public LuauTable removeTable(Key key)
    {
        LuauTable previous = tables.remove(key);
        if (widget != null)
            widget.handleEvents(OnDataChanged.class, _ -> true, new OnDataChanged.Table(key, previous, null, true));
        return previous;
    }

    @Override
    public CustomData merge(CustomData left, CustomData right)
    {
        CustomData data = new CustomData();
        data.doubles.putAll(left.doubles);
        data.doubles.putAll(right.doubles);
        data.ints.putAll(left.ints);
        data.ints.putAll(right.ints);
        data.strings.putAll(left.strings);
        data.strings.putAll(right.strings);
        data.flags.putAll(left.flags);
        data.flags.putAll(right.flags);
        data.tables.putAll(left.tables);
        data.tables.putAll(right.tables);
        return data;
    }

    @Override
    public String toString()
    {
        return "CustomData{" + "nums=" + doubles + ", ints=" + ints + ", strings=" + strings + ", flags=" + flags + ", tables=" + tables + '}';
    }
}
