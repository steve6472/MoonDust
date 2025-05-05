package steve6472.moondust.luau.global;

import org.joml.Vector2i;
import steve6472.flare.input.UserInput;
import steve6472.radiant.LuauMetaTable;
import steve6472.radiant.LuauUserObject;
import steve6472.radiant.func.OverloadFuncArgs;

/**
 * Created by steve6472
 * Date: 3/29/2025
 * Project: RadiantTest <br>
 */
public class LuaInput
{
    public static final LuauMetaTable META = new LuauMetaTable("Input");

    public static LuauUserObject createObject(UserInput input)
    {
        return new LuauUserObject(input, META);
    }

    public static void setup()
    {
        META.setInheritance(true);
        META.addOverloadedFunc("isKeyPressed", args().user("Input").number(), state -> {
            UserInput input = (UserInput) state.checkUserDataArg(1, "Input");
            int key = state.checkIntegerArg(2);
            state.pushBoolean(input.isKeyPressed(key));
            return 1;
        });
        META.addOverloadedFunc("getMousePositionRelativeToTopLeftOfTheWindow", args().user("Input"), state -> {
            UserInput input = (UserInput) state.checkUserDataArg(1, "Input");
            Vector2i pixelMouseLocOnScreen = input.getMousePositionRelativeToTopLeftOfTheWindow();
            state.pushNumber(pixelMouseLocOnScreen.x);
            state.pushNumber(pixelMouseLocOnScreen.y);
            return 2;
        });
        META.processOverloadedFunctions();
    }

    private static OverloadFuncArgs args()
    {
        return new OverloadFuncArgs();
    }
}
