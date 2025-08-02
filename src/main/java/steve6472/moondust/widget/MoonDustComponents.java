package steve6472.moondust.widget;

import com.mojang.serialization.Codec;
import net.hollowcube.luau.LuaState;
import steve6472.core.registry.Key;
import steve6472.moondust.ComponentEntry;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.widget.component.*;
import steve6472.radiant.LuauMetaTable;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by steve6472
 * Date: 3/31/2025
 * Project: MoonDust <br>
 */
public final class MoonDustComponents
{
    private MoonDustComponents() {}

    private static final Map<Key, ComponentEntry> COMPONENTS = new HashMap<>();
    private static final Map<Class<?>, ComponentEntry> BY_TYPE = new HashMap<>();

    public static void init()
    {
        register("sprites", Sprites.CODEC, Sprites.class);
        register("current_sprite", CurrentSprite.CODEC, CurrentSprite.class);
        register("styles", Styles.CODEC, Styles.class);
        register("focused_sprite", FocusedSprite.CODEC, FocusedSprite.class);
        register("bounds", Bounds.CODEC, Bounds.class);
        register("clickbox_size", ClickboxSize.CODEC, ClickboxSize.class);
        register("clickbox_offset", ClickboxOffset.CODEC, ClickboxOffset.class);
        register("text", MDText.CODEC, MDText.class);
        register("scripts", Scripts.CODEC, Scripts.class);
        register("sprite_size", SpriteSize.CODEC, SpriteSize.class);
        register("children", Children.CODEC, Children.class);
        register("z_index", ZIndex.CODEC, ZIndex.class);
        // no worky 'cause typed... uhhh annoying
//        register("position", Position.CODEC, Position.class);
    }

    private static <T> void register(String id, Codec<T> codec, Class<T> clazz)
    {
        Key key = MoonDustConstants.key(id);
        ComponentEntry entry = new ComponentEntry(key, codec, clazz, Optional.empty());
        COMPONENTS.put(key, entry);
        BY_TYPE.put(clazz, entry);
    }

    // not working for reason: too hard to do
    private static <T> void register(String id, Codec<T> codec, Class<T> clazz, LuauMetaTable meta)
    {
        Key key = MoonDustConstants.key(id);
        ComponentEntry entry = new ComponentEntry(key, codec, clazz, Optional.of(meta));
        COMPONENTS.put(key, entry);
        BY_TYPE.put(clazz, entry);
    }

    public static Class<?> get(LuaState state, int index)
    {
        String string = state.toString(index);
        Key key = Key.parse(string);
        ComponentEntry componentEntry = COMPONENTS.get(key);
        if (componentEntry == null)
            throw new NoSuchElementException("No component registered for '%s'".formatted(string));
        return componentEntry.clazz();
    }

    public static ComponentEntry byType(Class<?> clazz)
    {
        ComponentEntry componentEntry = BY_TYPE.get(clazz);
        if (componentEntry == null)
            throw new NoSuchElementException("No component registered for class '%s'".formatted(clazz));
        return componentEntry;
    }

    public static ComponentEntry byKey(Key key)
    {
        ComponentEntry componentEntry = COMPONENTS.get(key);
        if (componentEntry == null)
            throw new NoSuchElementException("No component registered for key '%s'".formatted(key));
        return componentEntry;
    }
}
