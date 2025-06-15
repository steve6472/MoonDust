package steve6472.moondust.blueprints;

import com.mojang.serialization.Codec;
import net.hollowcube.luau.LuaState;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.luau.ProfiledScript;
import steve6472.moondust.widget.MoonDustComponents;
import steve6472.moondust.widget.component.CustomData;
import steve6472.radiant.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 6/9/2025
 * Project: MoonDust <br>
 */
public class CustomBlueprint implements Keyable, Blueprint
{
    public static boolean LOG_CREATION = false;
    private static final Logger LOGGER = Log.getLogger(CustomBlueprint.class);
    public final BlueprintStructure structure;
    public final Key key;
    public final Object input;

    public CustomBlueprint(BlueprintStructure structure, Key key, Object input)
    {
        this.structure = structure;
        this.key = key;
        this.input = input;
    }

    @Override
    public Key key()
    {
        return key;
    }

    @Override
    public List<?> createComponents()
    {
        if (LOG_CREATION)
            LOGGER.info("Creating components for " + key + " with input data: " + input);
        ProfiledScript profiledScript = MoonDustRegistries.LUA_SCRIPTS.get(structure.script());
        if (profiledScript == null)
            throw new RuntimeException("Script '%s' not found for custom blueprint '%s'".formatted(structure.script(), key));

        List<?> objects = runInit(profiledScript);
        if (objects != null)
            return objects;

        return List.of();
    }

    private List<?> runInit(ProfiledScript profiledScript)
    {
        profiledScript.profiler().start();

        LuauScript script = profiledScript.script();
        LuaState state = script.state();

        // select function to run
        state.getGlobal("init");

        // test if it is a function
        if (!state.isFunction(-1))
        {
            LOGGER.severe("Error: 'init' is not a function, can not create components");
            LOGGER.severe("It either does not exist or is local");
            return null;
        }

        if (input instanceof LuauUserObject userObject)
        {
            userObject.pushUserObject(state);
        } else
        {
            LuauUtil.push(state, input);
        }
        state.pcall(1, 1);

        if (state.isNil(-1))
        {
            state.pop(0);
            profiledScript.profiler().end();
            return null;
        }

        LuauTable output = new LuauTable();
        output.readTable(state, -1);

        state.pop(1);

        profiledScript.profiler().end();

        List<Object> components = new ArrayList<>(output.table().size());

        for (Map.Entry<Object, Object> entry : output.table().entrySet())
        {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if (!(key instanceof String keyStr))
                throw new IllegalStateException("Component key has to be a string!");

            if (Key.parse(MoonDustConstants.NAMESPACE, keyStr).toString().equals("moondust:tables"))
            {
                CustomData data = new CustomData();
                LuauTable tableVal = (LuauTable) value;
                tableVal.table().forEach((lKey, lValue) -> {
                    data.putTable(Key.parse(lKey.toString()), ((LuauTable) lValue));
                });
                components.add(data);
                continue;
            }

            Codec<?> codec = MoonDustComponents.byKey(Key.parse(MoonDustConstants.NAMESPACE, keyStr)).codec();
            var decode = codec.decode(LuaTableOps.INSTANCE, value);
            Object comp = decode.getOrThrow().getFirst();
            components.add(comp);
        }

        return components;
    }
}
