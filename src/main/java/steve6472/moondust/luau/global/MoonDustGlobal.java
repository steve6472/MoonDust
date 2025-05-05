package steve6472.moondust.luau.global;

import net.hollowcube.luau.BuilinLibrary;
import net.hollowcube.luau.LuaState;
import net.hollowcube.luau.LuaType;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.moondust.luau.libraries.ColorUtilLib;
import steve6472.moondust.luau.libraries.MoonDustLib;
import steve6472.moondust.widget.UIEventEnum;
import steve6472.radiant.*;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 5/4/2025
 * Project: MoonDust <br>
 */
public class MoonDustGlobal
{
    private static final Logger LOGGER = Log.getLogger(MoonDustGlobal.class);
//    private static final Logger LUA_PRINT = Log.getLogger("Lua");

    public static final LuauMetaTable eventsMeta = new LuauMetaTable("eventsMeta");
    public static final LuauMetaTable event = new LuauMetaTable("event");

    private static boolean setupDone = false;

    private MoonDustGlobal () {}

    /// @param key used for print function
    public static LuauGlobal createGlobal(Key key)
    {
        LuauGlobal global = LuauGlobal.newGlobal();

        if (!setupDone)
        {
            setupDone = true;

            LuaWidget.setup();
            LuaInput.setup();
            LuaKey.setup();
            LuaKeyMap.setup();
            LuaCustomData.setup();
            createEvents();
        }

        global.openLibs(
            BuilinLibrary.BASE,
            BuilinLibrary.BIT32,
            BuilinLibrary.BUFFER,
            BuilinLibrary.COROUTINE,
            BuilinLibrary.MATH,
            BuilinLibrary.STRING,
            BuilinLibrary.TABLE,
            BuilinLibrary.UTF8
        );

        global.registerLib(CoreLib.INSTANCE);
        global.registerLib(ColorUtilLib.INSTANCE);
        global.registerLib(MoonDustLib.INSTANCE);

        global.addMetaTable(LuaWidget.META);
        global.addMetaTable(LuaInput.META);
        global.addMetaTable(LuaKey.META);
        global.addMetaTable(LuaCustomData.META);
//        global.addMetaTable(new LuauMetaTable("___TheThingCrashesWithoutAddingADummyTableLol"));

        global.addMetaTable(eventsMeta);
        global.addMetaTable(event);

        LuauTable globalValues = new LuauTable();
//        globalValues.add("screen_width", RadTestMain.SCREEN_WIDTH);
//        globalValues.add("screen_height", RadTestMain.SCREEN_HEIGHT);
        globalValues.pushTable(global.state());
        global.state().setGlobal("GLOBAL_VALUES");

        LuaKeyMap.KEYS.pushTable(global.state());
        global.state().setGlobal("Keys");

        // Create logger for this script specifically for debug
        Logger log = Log.getLogger("Lua - " + key);
        // Override default print function
        global.registerFunction("print", state -> {
            StringBuilder bob = new StringBuilder();
            for (int i = 1; i <= state.getTop(); i++)
            {
                bob.append(LuauUtil.toString(state, i));
                if (i != state.getTop())
                    bob.append("    ");
            }
            log.fine(bob.toString());
            return 0;
        });

        global.state().registerLib("string", Map.of("replace", state -> {
            String value = state.checkStringArg(1);
            String target = state.checkStringArg(2);
            String replacement = state.checkStringArg(3);
            state.pushString(value.replace(target, replacement));
            return 1;
        }));

        return global;
    }

    public static void addEventsRegistry(LuaState state)
    {
        LuauTable events = new LuauTable();
        events.setMetaTable(eventsMeta);

        for (UIEventEnum value : UIEventEnum.values())
        {
            events.add(value.id(), createEventTable());
        }

        events.pushTable(state);
        state.setGlobal("events");
    }

    private static LuauTable createEventTable()
    {
        LuauTable table = new LuauTable();
        table.setMetaTable(event);
        return table;
    }

    private static void createEvents()
    {
        eventsMeta.setInheritance(true);
        eventsMeta.addMetaMethod("__newindex", state -> {
            state.error("Attempt to modify read-only table");
            return 0;
        });

        event.setInheritance(true);
        event.addFunction("register", state -> {
            state.checkType(1, LuaType.TABLE);
            state.checkType(2, LuaType.FUNCTION);

            int count = 0;
            if (state.isTable(1))
            {
                state.pushNil();
                while (state.next(1))
                {
                    count++;
                    state.pop(1);
                }
            }

            state.pushInteger(count + 1);
            state.cloneFunction(2);
            state.setTable(-4);
            state.pop(2);
            return 0;
        });
        //        event.addMetaMethod("__newindex", state -> {
        //            state.error("Attempt to modify read-only table");
        //            return 0;
        //        });
    }

    /// @return false if some error was detected
    public static boolean runEvent(LuauScript script, String event, Object... arguments)
    {
        LuaState state = script.state();
        state.getGlobal("events");
        if (!state.isTable(-1))
        {
            //state.error crashes the JVM lol
            //state.error("Error: 'events' is not a table");
            LOGGER.severe("Error: 'events' is not a table");
            state.pop(1);
            return false;
        }

        state.getField(-1, event);
        if (!state.isTable(-1))
        {
            //state.error("Error: '%s' is not a valid event table\n".formatted(event));
            LOGGER.severe("Error: '%s' is not a valid event table\n".formatted(event));
            state.pop(1);
            return false;
        }

        state.pushNil();
        while (state.next(-2))
        {
            if (state.isFunction(-1))
            {
                state.pushValue(-1);
                for (Object argument : arguments)
                {
                    if (argument instanceof LuauUserObject userObject)
                    {
                        userObject.pushUserObject(state);
                    } else
                    {
                        LuauUtil.push(state, argument);
                    }
                }
                state.pcall(arguments.length, 0);
                state.pop(1);
            }
        }

        state.pop(2);
        return true;
    }
}
