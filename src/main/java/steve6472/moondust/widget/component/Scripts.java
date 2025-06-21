package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import steve6472.moondust.core.Mergeable;
import steve6472.moondust.widget.blueprint.ScriptEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public record Scripts(Map<String, ScriptEntry> scripts) implements Mergeable<Scripts>
{
    public static final Codec<Scripts> CODEC = Codec.unboundedMap(Codec.STRING, ScriptEntry.CODEC).xmap(Scripts::new, Scripts::scripts);

    @Override
    public Scripts merge(Scripts left, Scripts right)
    {
        Map<String, ScriptEntry> map = new HashMap<>(left.scripts);
        map.putAll(right.scripts);
        return new Scripts(map);
    }

    public ScriptEntry get(String id)
    {
        return scripts.get(id);
    }
}
