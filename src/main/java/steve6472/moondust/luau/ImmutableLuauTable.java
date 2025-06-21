package steve6472.moondust.luau;

import net.hollowcube.luau.LuaState;
import steve6472.radiant.LuauMetaTable;
import steve6472.radiant.LuauTable;

import java.util.Map;

/**
 * Created by steve6472
 * Date: 6/20/2025
 * Project: MoonDust <br>
 */
public class ImmutableLuauTable extends LuauTable
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

    private Map<Object, Object> realTable()
    {
        return super.table();
    }

    public static ImmutableLuauTable of(Object key1, Object value1)
    {
        ImmutableLuauTable table = new ImmutableLuauTable();
        table.realTable().put(key1, value1);
        return table;
    }

    public static ImmutableLuauTable of(Object key1, Object value1, Object key2, Object value2)
    {
        ImmutableLuauTable table = new ImmutableLuauTable();
        table.realTable().put(key1, value1);
        table.realTable().put(key2, value2);
        return table;
    }
}
