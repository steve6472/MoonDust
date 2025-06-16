package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.hollowcube.luau.LuaState;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDust;
import steve6472.radiant.LuauMetaTable;
import steve6472.radiant.LuauTable;

import java.util.Map;

/**
 * Created by steve6472
 * Date: 6/16/2025
 * Project: MoonDust <br>
 */
public record ScriptEntry(Key script, Object input)
{
    public static final LuauTable EMPTY = new ImmutableLuauTable();

    private static final Codec<ScriptEntry> CODEC_KEY = Key.CODEC.xmap(k -> new ScriptEntry(k, EMPTY), s -> s.script);

    private static final Codec<ScriptEntry> CODEC_INPUT = RecordCodecBuilder.create(instance -> instance
        .group(Key.CODEC.fieldOf("script").forGetter(ScriptEntry::script), MoonDust.CODEC_LUA_VALUE
            .optionalFieldOf("input", EMPTY)
            .forGetter(ScriptEntry::input))
        .apply(instance, ScriptEntry::new));

    public static final Codec<ScriptEntry> CODEC = Codec.withAlternative(CODEC_INPUT, CODEC_KEY);

    public ScriptEntry
    {
        if (input instanceof LuauTable table)
            fixLuauTableNumbers(table);
        else if (input instanceof Number num && !(input instanceof Double))
            input = num.intValue();
    }

    public ScriptEntry(Key script)
    {
        this(script, EMPTY);
    }

    private void fixLuauTableNumbers(LuauTable table)
    {
        for (Map.Entry<Object, Object> entry : table.table().entrySet())
        {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Number num)
            {
                value = num.doubleValue();
            }

            if (key instanceof Number num)
            {
                key = num.intValue();
            }

            table.add(key, value);
        }
    }

    private static class ImmutableLuauTable extends LuauTable
    {
        @Override
        public void setMetaTable(LuauMetaTable metaTable)
        {
            throw new RuntimeException("Immutable table can not be modified");
        }

        @Override
        public LuauTable add(Object key, Object value)
        {
            throw new RuntimeException("Immutable table can not be modified");
        }

        @Override
        public void readTable(LuaState state, int startIndex)
        {
            throw new RuntimeException("Immutable table can not be modified");
        }

        @Override
        public Map<Object, Object> table()
        {
            // returns immutable copy
            return Map.copyOf(super.table());
        }
    }
}
