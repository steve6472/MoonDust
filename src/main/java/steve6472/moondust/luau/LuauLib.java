package steve6472.moondust.luau;

import net.hollowcube.luau.LuaFunc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 1/15/2025
 * Project: MoonDust <br>
 */
public abstract class LuauLib
{
    Map<String, LuaFunc> functions = new HashMap<>();

    public abstract void createFunctions();
    public abstract String name();

    protected void registerFunction(String name, LuaFunc func)
    {
        functions.put(name, func);
    }
}
