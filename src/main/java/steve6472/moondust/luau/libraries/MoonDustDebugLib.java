package steve6472.moondust.luau.libraries;

import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.flare.registry.FlareRegistries;
import steve6472.flare.ui.font.render.TextPart;
import steve6472.flare.ui.font.style.FontStyleEntry;
import steve6472.moondust.DebugWidgetUILines;
import steve6472.moondust.MoonDust;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.MDText;
import steve6472.radiant.LuauLib;
import steve6472.test.DebugUILines;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 5/5/2025
 * Project: MoonDust <br>
 */
public class MoonDustDebugLib extends LuauLib
{
    private static final Logger LOGGER = Log.getLogger(MoonDustDebugLib.class);
    public static final MoonDustDebugLib INSTANCE = new MoonDustDebugLib();

    private MoonDustDebugLib() {}

    @Override
    public void createFunctions()
    {
        addFunction("setWidgetUiLines", state -> {
            String property = state.checkStringArg(1);
            boolean value = state.checkBooleanArg(2);
            switch (property)
            {
                case "bounds" -> DebugWidgetUILines.bounds = value;
                case "spriteSize" -> DebugWidgetUILines.spriteSize = value;
                case "clickbox" -> DebugWidgetUILines.clickbox = value;
                default -> throw new IllegalStateException("Unexpected value: " + property);
            }
            return 0;
        });

        addFunction("getWidgetUiLines", state -> {
            String property = state.checkStringArg(1);
            switch (property)
            {
                case "bounds" -> state.pushBoolean(DebugWidgetUILines.bounds);
                case "spriteSize" -> state.pushBoolean(DebugWidgetUILines.spriteSize);
                case "clickbox" -> state.pushBoolean(DebugWidgetUILines.clickbox);
                default -> throw new IllegalStateException("Unexpected value: " + property);
            }
            return 1;
        });

        // Cursed shit 'cause I'm lazy to change Flare
        List<DebugUILines.DebugOption> textOptions = new ArrayList<>();

        try {
            Field options = DebugUILines.class.getDeclaredField("OPTIONS");
            options.setAccessible(true);
            Object o = options.get(null);
            //noinspection unchecked
            textOptions.addAll((List<DebugUILines.DebugOption>) o);
        } catch (NoSuchFieldException | IllegalAccessException e)
        {
            LOGGER.severe("Could not create text lines debug functions");
        }

        addFunction("setTextLines", state -> {
            String property = state.checkStringArg(1);
            boolean value = state.checkBooleanArg(2);
            Optional<DebugUILines.DebugOption> first = textOptions
                .stream()
                .filter(o -> o.setting().key().toString().equals(property))
                .findFirst();
            first.ifPresent(o -> o.setting().set(value));
            return 0;
        });

        addFunction("getTextLines", state -> {
            String property = state.checkStringArg(1);
            Optional<DebugUILines.DebugOption> first = textOptions
                .stream()
                .filter(o -> o.setting().key().toString().equals(property))
                .findFirst();
            state.pushBoolean(first.isPresent() && first.get().get());
            return 1;
        });
    }

    @Override
    public String name()
    {
        return "MoonDustDebug";
    }
}
