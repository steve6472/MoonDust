package steve6472.moondust.view.property;

import steve6472.radiant.LuauTable;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public class TableProperty extends Property<LuauTable>
{
    public TableProperty()
    {
        this(new LuauTable());
    }

    public TableProperty(LuauTable value)
    {
        set(value);
    }

    @Override
    public LuauTable get()
    {
        LuauTable oldTable = super.get();
        LuauTable newTable = new LuauTable();
        oldTable.table().forEach(newTable::add);
        return newTable;
    }
}
