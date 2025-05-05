package steve6472.moondust.luau;

import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;
import steve6472.core.util.Profiler;
import steve6472.moondust.luau.global.MoonDustGlobal;
import steve6472.radiant.LuauScript;
import steve6472.radiant.LuauUserObject;

import java.util.logging.Logger;

public final class ProfiledScript implements Keyable
{
    private static final Logger LOGGER = Log.getLogger(ProfiledScript.class);

    private final LuauScript script;
    private final String numberedScript;
    private final Profiler profiler;
    private final Key key;
    private boolean enabled = true;

    ProfiledScript(LuauScript script, Profiler profiler, Key key, String numberedScript)
    {
        this.script = script;
        this.profiler = profiler;
        this.key = key;
        this.numberedScript = numberedScript;
    }

    public LuauScript script()
    {
        return script;
    }

    public Profiler profiler()
    {
        return profiler;
    }

    @Override
    public Key key()
    {
        return key;
    }

    public boolean enabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void prime()
    {
        try
        {
            script.run();
        } catch (Exception e)
        {
            LOGGER.info("Script that failed ->\n" + numberedScript);
            throw new RuntimeException(e);
        }
    }

    public void run(String eventName, Object... objects)
    {
        if (!enabled())
            return;

        profiler().start();
        LuauScript renderScript = script();
        try
        {
            boolean event = MoonDustGlobal.runEvent(renderScript, eventName, objects);
            if (!event)
            {
                LOGGER.severe("Error in script '%s'".formatted(key()));
                setEnabled(false);
            }
        } catch (Throwable ex)
        {
            LOGGER.info("Script that failed ->\n" + numberedScript);
            //noinspection CallToPrintStackTrace
            ex.printStackTrace();
        }
        profiler().end();
    }
}