package steve6472.moondust.builtin;

import steve6472.core.registry.Key;
import steve6472.flare.core.FlareApp;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.InputWithWidget;
import steve6472.moondust.core.JavaFunc;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.Name;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by steve6472
 * Date: 6/21/2025
 * Project: MoonDust <br>
 */
public class JavaFunctions
{
    public static void init(FlareApp app)
    {
        register("button/exit_app", _ -> app.window().closeWindow());

        register("button/random_toggle_enabled", input ->
        {
            if (input instanceof InputWithWidget widgetInput)
            {
                Widget widget = widgetInput.widget();
                widget.setEnabled(!widget.isEnabled());
//                System.out.println("Toggled to: " + widget.isEnabled());
            }
        });

        register("button/test_press", input ->
        {
            if (input instanceof InputWithWidget widgetInput)
            {
                Widget widget = widgetInput.widget();
                widget
                    .getComponent(Name.class)
                    .ifPresentOrElse(name -> System.out.println("Button '" + name.value() + "' pressed!"), () -> System.out.println("Unnamed Button pressed!"));
            }
        });
    }

    private static void registerRet(Key key, Function<Object, Object> function)
    {
        MoonDustRegistries.JAVA_FUNC.register(new JavaFunc(key, function));
    }

    private static void registerRet(String key, Function<Object, Object> function)
    {
        MoonDustRegistries.JAVA_FUNC.register(new JavaFunc(MoonDustConstants.key(key), function));
    }

    private static void register(String key, Consumer<Object> function)
    {
        MoonDustRegistries.JAVA_FUNC.register(new JavaFunc(MoonDustConstants.key(key), o ->
        {
            function.accept(o);
            return null;
        }));
    }
}
