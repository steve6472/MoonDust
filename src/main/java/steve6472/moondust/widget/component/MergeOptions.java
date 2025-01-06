package steve6472.moondust.widget.component;

import steve6472.core.registry.Key;
import steve6472.moondust.widget.blueprint.MergeOptionsBlueprint;

import java.util.Map;

/**
 * Created by steve6472
 * Date: 1/6/2025
 * Project: MoonDust <br>
 */
public record MergeOptions(Map<Key, MergeOptionsBlueprint.Option> sprites)
{
    public static final MergeOptions DEFAULT = new MergeOptions(Map.of());

    public boolean shouldMerge(Key blueprint)
    {
        return sprites.getOrDefault(blueprint, MergeOptionsBlueprint.Option.MERGE) == MergeOptionsBlueprint.Option.MERGE;
    }
}
