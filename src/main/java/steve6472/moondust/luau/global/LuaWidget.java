package steve6472.moondust.luau.global;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.hollowcube.luau.LuaType;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.moondust.ComponentEntry;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.widget.MoonDustComponents;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.CustomData;
import steve6472.moondust.widget.component.InternalStates;
import steve6472.radiant.*;
import steve6472.radiant.func.OverloadFuncArgs;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 3/31/2025
 * Project: MoonDust <br>
 */
public class LuaWidget
{
    private static final Logger LOGGER = Log.getLogger(LuaWidget.class);
    public static final LuauMetaTable META = new LuauMetaTable("Widget");

    public static LuauUserObject createObject(Widget widget)
    {
        return new LuauUserObject(widget, META);
    }

    public static void setup()
    {
        META.setInheritance(true);
        META.addFunction("getComponent", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            String type = state.checkStringArg(2);
            Key typeKey = Key.parse(MoonDustConstants.NAMESPACE, type);

            ComponentEntry componentEntry = MoonDustComponents.byKey(typeKey);
            Optional<?> component = widget.getComponent(componentEntry.clazz());
            if (component.isEmpty())
            {
                LOGGER.warning("Failed to get component %s".formatted(typeKey));
                state.pushNil();
                return 1;
            }
            Object lua = componentEntry.toLua(component.get());
            LuauUtil.push(state, lua);
            return 1;
        });
        META.addFunction("getTable", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            Key key = Key.parse(state.checkStringArg(2));
            widget.customData().getTable(key).pushTable(state);
            return 1;
        });
        META.addFunction("addComponent", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            String type = state.checkStringArg(2);
            Key typeKey = Key.parse(MoonDustConstants.NAMESPACE, type);

            Object java = LuauUtil.toJava(state, 3);

            Codec<?> codec = MoonDustComponents.byKey(typeKey).codec();
            var decode = codec.decode(LuaTableOps.INSTANCE, java);
            Object first = decode.getOrThrow().getFirst();
            widget.addComponent(first);
            return 0;
        });
        META.addFunction("setTable", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            Key key = Key.parse(state.checkStringArg(2));
            LuauTable luauTable = new LuauTable();
            luauTable.readTable(state, 3);
            widget.customData().putTable(key, luauTable);
            return 1;
        });
        META.addFunction("removeTable", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            Key key = Key.parse(state.checkStringArg(2));
            LuauUtil.push(state, widget.customData().removeTable(key));
            return 1;
        });

        // Getters
        META.addFunction("getChild", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            Optional<Widget> child = widget.getChild(state.checkStringArg(2));
            if (child.isPresent())
            {
                LuaWidget.createObject(child.get()).pushUserObject(state);
            } else
            {
                state.pushNil();
            }
            return 1;
        });
        META.addFunction("getChildrenNames", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            LuauTable names = new LuauTable();
            int i = 1;
            for (Widget child : widget.getChildren())
            {
                names.add(i, child.getName());
                i++;
            }
            names.pushTable(state);
            return 1;
        });
        META.addFunction("getParent", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            Optional<Widget> parent = widget.parent();
            if (parent.isPresent())
            {
                LuaWidget.createObject(parent.get()).pushUserObject(state);
            } else
            {
                state.pushNil();
            }
            return 1;
        });
        META.addFunction("internalStates", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");

            //noinspection unchecked,rawtypes
            DataResult<Object> objectDataResult = ((Codec) InternalStates.CODEC).encodeStart(LuaTableOps.INSTANCE, widget.internalStates());
            Object lua = objectDataResult.getOrThrow();
            LuauUtil.push(state, lua);
            return 1;
        });
        META.addFunction("customData", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            LuaCustomData.createObject(widget.customData()).pushUserObject(state);
            return 1;
        });
        META.addFunction("getName", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            state.pushString(widget.getName());
            return 1;
        });
        META.addFunction("getPath", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            state.pushString(widget.getPath());
            return 1;
        });
        META.addFunction("isVisible", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            state.pushBoolean(widget.isVisible());
            return 1;
        });
        META.addFunction("isEnabled", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            state.pushBoolean(widget.isEnabled());
            return 1;
        });
        META.addFunction("isClickable", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            state.pushBoolean(widget.isClickable());
            return 1;
        });
        META.addFunction("isFocusable", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            state.pushBoolean(widget.isFocusable());
            return 1;
        });

        // Setters
        /*META.addFunction("setCustomData", state -> {
            LOGGER.warning("setCustomData has to be reworked");
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            LuauTable table = new LuauTable();
            table.readTable(state, 2);

            Codec<?> codec = MoonDustComponents.byType(CustomData.class).codec();
            var decode = codec.decode(LuaTableOps.INSTANCE, table);
            CustomData newData = (CustomData) decode.getOrThrow().getFirst();
            CustomData data = widget.customData();
            data.doubles.forEach((k, v) -> {
                if (!newData.doubles.containsKey(k))
                    data.removeDouble(k);
                else
                    data.putDouble(k, newData.doubles.getDouble(k));
            });
            data.ints.forEach((k, v) -> {
                if (!newData.ints.containsKey(k))
                    data.removeInt(k);
                else
                    data.putInt(k, newData.ints.getInt(k));
            });
            data.strings.forEach((k, v) -> {
                if (!newData.strings.containsKey(k))
                    data.removeString(k);
                else
                    data.putString(k, newData.strings.get(k));
            });
            data.flags.forEach((k, v) -> {
                if (!newData.flags.containsKey(k))
                    data.removeFlag(k);
                else
                    data.putFlag(k, newData.flags.getBoolean(k));
            });
            return 1;
        });*/
        META.addFunction("setVisible", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            widget.setVisible(state.checkBooleanArg(2));
            return 0;
        });
        META.addFunction("setEnabled", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            widget.setEnabled(state.checkBooleanArg(2));
            return 0;
        });
        META.addFunction("setClickable", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            widget.setClickable(state.checkBooleanArg(2));
            return 0;
        });
        META.addFunction("setFocusable", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            widget.setFocusable(state.checkBooleanArg(2));
            return 0;
        });
        META.addFunction("setBounds", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            widget.setBounds(state.checkIntegerArg(2), state.checkIntegerArg(3));
            return 0;
        });
        META.processOverloadedFunctions();
    }

    private static OverloadFuncArgs args()
    {
        return new OverloadFuncArgs();
    }
}
