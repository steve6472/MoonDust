package steve6472.moondust.luau;

import net.hollowcube.luau.compiler.LuauCompileException;
import net.hollowcube.luau.compiler.LuauCompiler;
import steve6472.core.log.Log;
import steve6472.core.module.Module;
import steve6472.core.module.ResourceCrawl;
import steve6472.core.registry.Key;
import steve6472.core.util.Profiler;
import steve6472.flare.core.Flare;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.luau.global.MoonDustGlobal;
import steve6472.radiant.LuauGlobal;
import steve6472.radiant.LuauScript;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve6472
 * Date: 3/31/2025
 * Project: MoonDust <br>
 */
public class LuaScriptLoader
{
    public static boolean DEBUG_SCRIPT_LOAD_PRINT = false;
    public static boolean DEBUG_META = false;

    private static final Logger LOGGER = Log.getLogger(LuaScriptLoader.class);

    public static final Pattern PATTERN_NO_SCRIPT = Pattern.compile("^--#noscript\\s*\n");
    public static final Pattern PATTERN_INCLUDE = Pattern.compile("--#include\\s+([a-z0-9_/]*)?:?([a-z0-9_/]*)\\s*\\n");

    private final static List<LuauGlobal> GLOBALS = new ArrayList<>();

    private record WithModule<T>(Module module, T obj) {}

    public static void load()
    {
        Map<Key, WithModule<String>> cache = new HashMap<>();

        for (Module module : Flare.getModuleManager().getModules())
        {
            module.iterateNamespaces((folder, namespace) -> {
                File file = new File(folder, "ui/script");

                ResourceCrawl.crawl(file, true, (filePath, id) -> {
                    Key key = Key.withNamespace(namespace, id);

                    try
                    {
                        LOGGER.finest("Reading %s '%s' from module '%s'".formatted("Luau script", key, module.name()));
                        cache.put(key, new WithModule<>(module, Files.readString(filePath.toPath()).trim()));
                    } catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                });
            });
        }

        Map<Key, WithModule<String>> processed = new HashMap<>();

        cache.forEach((key, withModule) -> {
            String script = withModule.obj;
            LOGGER.finest("Processing %s '%s' from module '%s'".formatted("Luau script", key, withModule.module.name()));
            String processedScript = processMeta(cache, new HashSet<>(), key, script, false);
            if (processedScript != null)
                processed.put(key, new WithModule<>(withModule.module, processedScript));
        });

        LOGGER.finest("Priming stage");

        Profiler profiler = new Profiler(1);
        profiler.start();
        processed.forEach((key, script) -> {
            if (DEBUG_SCRIPT_LOAD_PRINT)
                LOGGER.info("Processed script %s -> \n%s".formatted(key, addLines(script.obj)));

            ProfiledScript profiledScript = createScript(script.module, script.obj, key);
            try
            {
                profiledScript.prime();
            } catch (Exception ex)
            {
                LOGGER.severe("Error while priming script '" + key + "'");
                throw new RuntimeException(ex);
            }

            MoonDustRegistries.LUA_SCRIPTS.register(profiledScript);
        });
        profiler.end();
        LOGGER.fine("%s in: %s ms".formatted("Scripts primed", profiler.lastMilli()));
    }

    private static String addLines(String script)
    {
        if (script == null || script.isEmpty()) return "";

        String[] lines = script.split("\\r?\\n");
        StringBuilder numbered = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            numbered.append(i + 1).append(": ").append(lines[i]);
            if (i < lines.length - 1) {
                numbered.append("\n");
            }
        }

        return numbered.toString();
    }

    public static void cleanUp()
    {
        GLOBALS.forEach(LuauGlobal::close);
    }

    /// @return null if script is annotated with --#noscript and no script should be created in the registry
    private static String processMeta(Map<Key, WithModule<String>> cache, Set<Key> alreadyIncluded, Key scriptKey, String script, boolean ignoreNoScript)
    {
        if (!ignoreNoScript)
        {
            Matcher matcher = PATTERN_NO_SCRIPT.matcher(script);
            if (matcher.find())
            {
                if (DEBUG_META)
                    LOGGER.finest("Ignoring " + scriptKey);
                return null;
            }
        } else
        {
            script = script.replaceFirst(PATTERN_NO_SCRIPT.pattern(), "--ignored noscript\n");
        }

        // No file traversal allowed 'cause IDK how to make it secure and I'm too lazy too
        Matcher matcher = PATTERN_INCLUDE.matcher(script);
        while (matcher.find())
        {
            String namespace = matcher.group(1);
            String id = matcher.group(2);
            if (id.isEmpty())
            {
                id = namespace;
                namespace = scriptKey.namespace();
            }
            Key key = Key.withNamespace(namespace, id);

            if (alreadyIncluded.contains(key))
                continue;
            alreadyIncluded.add(key);

            WithModule<String> stringWithModule = cache.get(key);
            if (stringWithModule == null)
                throw new RuntimeException("Script %s does not exist".formatted(key));
            String includeString = stringWithModule.obj;
            if (includeString == null)
                throw new RuntimeException("Script %s does not exist".formatted(key));
            includeString = processMeta(cache, alreadyIncluded, key, includeString, true);
            if (includeString == null)
                throw new RuntimeException("Still null!? %s".formatted(key));
            script = script.replaceFirst(PATTERN_INCLUDE.pattern(), includeString + "\n");
        }
        return script;
    }

    private static ProfiledScript createScript(Module module, String code, Key key)
    {
        try
        {
            LOGGER.finest("Loading %s '%s' from module '%s'".formatted("Luau script", key, module.name()));
            byte[] bytecode = LuauCompiler.DEFAULT.compile(code);
            LuauGlobal global = MoonDustGlobal.createGlobal(key);
            GLOBALS.add(global);
            LuauScript script = global.createScript(key.toString(), bytecode);

            MoonDustGlobal.addEventsRegistry(script.state());
            //            GlobalSetup.global.state().pop(1);
            return new ProfiledScript(script, new Profiler(10), key, addLines(code));
        } catch (LuauCompileException e)
        {
            throw new RuntimeException(e);
        }
    }
}
