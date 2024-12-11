package steve6472.moondust.widget;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.widget.override.EventsOverride;
import steve6472.moondust.widget.override.OverrideEntry;
import steve6472.moondust.widget.override.SpritesOverride;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public class BlueprintOverrides
{
    public static final OverrideEntry<EventsOverride> EVENTS = register(EventsOverride.KEY, EventsOverride.CODEC);
    public static final OverrideEntry<SpritesOverride> SPRITES = register(SpritesOverride.KEY, SpritesOverride.CODEC);

    public static <T extends BlueprintOverride<?>> OverrideEntry<T> register(Key key, Codec<T> codec)
    {
        if (MoonDustRegistries.OVERRIDE.get(key) != null)
            throw new RuntimeException("Override with key " + key + " already exists!");

        OverrideEntry<T> obj = new OverrideEntry<>(key, codec);
        MoonDustRegistries.OVERRIDE.register(key, obj);
        return obj;
    }
}
