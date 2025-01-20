package steve6472.moondust.luau;

import net.hollowcube.luau.LuaFunc;
import net.hollowcube.luau.LuaState;
import net.hollowcube.luau.LuaType;
import net.hollowcube.luau.compiler.LuauCompiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 1/13/2025
 * Project: MoonDust <br>
 */
public class LuauLearning
{
    public static void main(String[] args) throws Exception
    {
        var source = """
            print('hello from lua')
            
            type Point = { x: number, y: number }
            
            local arr = m2.newarray()
            arr:push(1)
            arr:push(2)
            m2.show(arr)
            
            local p: Point = { x = 0x0f, y = 2 }
            
            function onEvent(event)
                print(event.fname)
            end
            
            function testEvent()
                print("Test Event")
            end
            
            local num = 42
            
            print(m2.add(1, 2))
            print(m2.sub(1, 2))
            abc()
            print("x:"..p.x.." y:"..p.y)
            p.x += 1
            print("x:"..p.x.." y:"..p.y)
            """;
        var bytecode = LuauCompiler.DEFAULT.compile(source);

        LuaState global = LuaState.newState();
        try
        {
            global.openLibs();

            global.newMetaTable("myarray");
            global.pushString("__index");
            global.pushValue(-2);
            global.setTable(-3);
            global.registerLib(null, Map.of("push", state ->
            {
                List<Integer> arr = (List<Integer>) state.checkUserDataArg(1, "myarray");
                int value = state.checkIntegerArg(2);
                arr.add(value);
                return 0;
            }));

            Map<String, LuaFunc> m2lib = Map.of("add", state ->
            {
                int left = state.checkIntegerArg(1);
                int right = state.checkIntegerArg(2);
                state.pushInteger(left + right);
                return 1;
            }, "sub", state ->
            {
                int left = state.checkIntegerArg(1);
                int right = state.checkIntegerArg(2);
                state.pushInteger(left - right);
                //                        state.error("error from java");
                return 1;
            }, "newarray", state ->
            {
                List<Integer> arr = new ArrayList<>();
                state.newUserData(arr);

                // Assign the metatable. todo: the library should just handle this IMO
                state.getMetaTable("myarray");
                state.setMetaTable(-2);

                return 1;
            }, "show", state ->
            {
                List<Integer> arr = (List<Integer>) state.checkUserDataArg(1, "myarray");
                System.out.println("array: " + arr);
                return 0;
            });

            global.registerLib("m2", m2lib);
            global.pushCFunction(_ ->
            {
                System.out.println("hello from java");
                return 0;
            }, "abc");
            global.setGlobal("abc");

            // After this point it is invalid to do any setglobal calls.
            // We should check this in java land because it segfaults.
            global.sandbox();

            LuaState thread = global.newThread();
            thread.sandboxThread();
            // Now ready for running untrusted code.
            thread.load("main.lua", bytecode);
            // ALWAYS do a priming run
            thread.pcall(0, 0);

            // Tell what function to run
            thread.getGlobal("onEvent");
            thread.newTable();
            thread.pushString("fname");
            thread.pushString("Margie");
            thread.setTable(-3);
            // run the function
            thread.pcall(1, 0);



            global.pop(1); // the thread was added to the stack, remove it so that it can be garbage collected.

        } finally
        {
            global.close();
        }
    }
}
