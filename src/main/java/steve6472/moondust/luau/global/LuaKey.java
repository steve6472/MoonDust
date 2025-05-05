package steve6472.moondust.luau.global;

import steve6472.core.registry.Key;
import steve6472.radiant.LuauMetaTable;
import steve6472.radiant.LuauTable;
import steve6472.radiant.func.OverloadFuncArgs;

/**
 * Created by steve6472
 * Date: 3/29/2025
 * Project: RadiantTest <br>
 */
public class LuaKey
{
    public static final LuauMetaTable META = new LuauMetaTable("Key");

    public static LuauTable createObject(Key key)
    {
        LuauTable table = new LuauTable();
        table.setMetaTable(META);
        table.add("namespace", key.namespace());
        table.add("id", key.id());
        return table;
    }

    public static void setup()
    {
        META.setInheritance(true);
        META.addMetaMethod("__tostring", state -> {
            LuauTable table = new LuauTable();
            table.readTable(state, 1);
            state.pushString("%s:%s".formatted(table.get("namespace"), table.get("id")));
            return 1;
        });
        META.addMetaMethod("__eq", state -> {
            LuauTable left = new LuauTable();
            left.readTable(state, 1);

            LuauTable right = new LuauTable();
            right.readTable(state, 2);

            state.pushBoolean(
                left.get("namespace").equals(right.get("namespace")) &&
                    left.get("id").equals(right.get("id")));
            return 1;
        });
        META.addOverloadedGlobalFunc("new", args().string(), state -> {
            String toParse = state.checkStringArg(1);
            createObject(Key.parse(toParse)).pushTable(state);
            return 1;
        });
        META.addOverloadedGlobalFunc("new", args().string().string(), state -> {
            String namespace = state.checkStringArg(1);
            String id = state.checkStringArg(2);
            createObject(Key.withNamespace(namespace, id)).pushTable(state);
            return 1;
        });
        META.processOverloadedFunctions();
    }

    private static OverloadFuncArgs args()
    {
        return new OverloadFuncArgs();
    }
}
