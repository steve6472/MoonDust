package steve6472.moondust.luau.global;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import steve6472.core.registry.Key;
import steve6472.moondust.widget.component.CustomData;
import steve6472.radiant.*;

public class LuaCustomData {
    public static final LuauMetaTable META = new LuauMetaTable("CustomData");

    public static LuauUserObject createObject(CustomData data)
    {
        return new LuauUserObject(data, META);
    }

    public static void setup()
    {
        META.setInheritance(true);

        // === GETTERS ===
        META.addFunction("getNumber", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            state.pushNumber(data.getDouble(key));
            return 1;
        });

        META.addFunction("getInt", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            state.pushInteger(data.getInt(key));
            return 1;
        });

        META.addFunction("getString", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            LuauUtil.push(state, data.getString(key));
            return 1;
        });

        META.addFunction("getFlag", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            state.pushBoolean(data.getFlag(key));
            return 1;
        });

        // === SETTERS ===
        META.addFunction("setNumber", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            double value = state.checkNumberArg(3);
            data.putDouble(key, value);
            return 0;
        });

        META.addFunction("setInt", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            int value = state.checkIntegerArg(3);
            data.putInt(key, value);
            return 0;
        });

        META.addFunction("setString", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            String value = state.checkStringArg(3);
            data.putString(key, value);
            return 0;
        });

        META.addFunction("setFlag", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            boolean value = state.checkBooleanArg(3);
            data.putFlag(key, value);
            return 0;
        });

        // === REMOVERS ===
        META.addFunction("removeNumber", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            state.pushNumber(data.removeDouble(key));
            return 1;
        });

        META.addFunction("removeInt", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            state.pushInteger(data.removeInt(key));
            return 1;
        });

        META.addFunction("removeString", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            LuauUtil.push(state, data.removeString(key));
            return 1;
        });

        META.addFunction("removeFlag", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            Key key = Key.parse(state.checkStringArg(2));
            state.pushBoolean(data.removeFlag(key));
            return 1;
        });

        // Optional: Clear All
        META.addFunction("clear", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            data.doubles.keySet().forEach(data::removeDouble);
            data.ints.keySet().forEach(data::removeInt);
            data.strings.keySet().forEach(data::removeString);
            data.flags.keySet().forEach(data::removeFlag);
            return 0;
        });

        META.addFunction("toTable", state -> {
            CustomData data = (CustomData) state.checkUserDataArg(1, "CustomData");
            //noinspection unchecked, rawtypes
            DataResult<Object> objectDataResult = ((Codec) CustomData.CODEC).encodeStart(LuaTableOps.INSTANCE, data);
            LuauTable table = (LuauTable) objectDataResult.getOrThrow();
            table.pushTable(state);
            return 1;
        });

        META.processOverloadedFunctions();
    }
}