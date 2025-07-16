package steve6472.moondust.builtin;

import org.jetbrains.annotations.NotNull;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.flare.FlareConstants;
import steve6472.flare.assets.TextureSampler;
import steve6472.flare.core.FlareApp;
import steve6472.flare.registry.FlareRegistries;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.InputWithWidget;
import steve6472.moondust.core.JavaFunc;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.Name;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 6/21/2025
 * Project: MoonDust <br>
 */
public class JavaFunctions
{
    private static final Logger LOGGER = Log.getLogger(JavaFunctions.class);

    public static void init(FlareApp app)
    {
        register("button/exit_app", input ->
        {
            LOGGER.info("Closing app with input: " + input);
            app.window().closeWindow();
        });

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

        register("button/dump_textures", input ->
        {
            LOGGER.info("Dumping textures");
            File file = getFile();

            LOGGER.info("Generating new textures");
            for (Key key : FlareRegistries.SAMPLER.keys())
            {
                File dumpFile = new File(file, key.namespace() + "-" + key.id().replaceAll("/", "__") + ".png");
                TextureSampler textureSampler = FlareRegistries.SAMPLER.get(key);
                textureSampler.texture.saveTextureAsPNG(app.device(), app.masterRenderer().getCommands(), app.masterRenderer()
                    .getGraphicsQueue(), dumpFile);
            }
            LOGGER.info("Finished dumping textures");
        });
    }

    private static @NotNull File getFile()
    {
        File file = new File(FlareConstants.FLARE_DEBUG_FOLDER, "dumped");
        if (file.exists())
        {
            LOGGER.info("Removing old textures");
            File[] files = file.listFiles();
            if (files != null)
            {
                for (File listFile : files)
                {
                    if (!listFile.delete())
                    {
                        LOGGER.severe("Could not delete " + listFile.getAbsolutePath());
                    }
                }
            }
        } else
        {
            if (!file.mkdirs())
            {
                LOGGER.severe("Could not create " + file.getAbsolutePath());
            }
        }
        return file;
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
