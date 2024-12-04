package steve6472.moondust.child;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.child.blueprint.NameBlueprint;
import steve6472.moondust.child.blueprint.SpriteSizeBlueprint;
import steve6472.moondust.child.blueprint.WidgetReferenceBlueprint;
import steve6472.moondust.child.blueprint.position.PositionBlueprint;
import steve6472.moondust.child.component.SpriteSize;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.widget.WidgetBlueprints;
import steve6472.moondust.widget.blueprint.generic.EnabledBlueprint;
import steve6472.moondust.widget.blueprint.generic.VisibleBlueprint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public class ChildBlueprints
{
    public static final List<BlueprintEntry<?>> REQUIRED_BLUEPRINTS = new ArrayList<>();

    public static final BlueprintEntry<NameBlueprint> NAME = registerRequired(NameBlueprint.KEY, NameBlueprint.CODEC);
    public static final BlueprintEntry<WidgetReferenceBlueprint> WIDGET = registerRequired(WidgetReferenceBlueprint.KEY, WidgetReferenceBlueprint.CODEC);
    public static final BlueprintEntry<PositionBlueprint> POSITION = registerRequired(PositionBlueprint.KEY, PositionBlueprint.CODEC);

    public static final BlueprintEntry<SpriteSizeBlueprint> SPRITE_SIZE = register(SpriteSizeBlueprint.KEY, SpriteSizeBlueprint.CODEC);

    // Copied from Widget
    public static final BlueprintEntry<EnabledBlueprint> ENABLED = register(WidgetBlueprints.ENABLED);
    public static final BlueprintEntry<VisibleBlueprint> VISIBLE = register(WidgetBlueprints.VISIBLE);

    /*
     * Register methods
     */

    private static <T extends Blueprint> BlueprintEntry<T> register(Key key, Codec<T> codec)
    {
        if (MoonDustRegistries.CHILD_BLUEPRINT.get(key) != null)
            throw new RuntimeException("Blueprint with key " + key + " already exists!");

        BlueprintEntry<T> obj = new BlueprintEntry<>(key, codec);
        MoonDustRegistries.CHILD_BLUEPRINT.register(key, obj);
        return obj;
    }

    private static <T extends Blueprint> BlueprintEntry<T> register(BlueprintEntry<T> existingEntry)
    {
        if (MoonDustRegistries.CHILD_BLUEPRINT.get(existingEntry.key()) != null)
            throw new RuntimeException("Blueprint with key " + existingEntry.key() + " already exists!");

        MoonDustRegistries.CHILD_BLUEPRINT.register(existingEntry);
        return existingEntry;
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerRequired(Key key, Codec<T> codec)
    {
        BlueprintEntry<T> register = register(key, codec);
        REQUIRED_BLUEPRINTS.add(register);
        return register;
    }
}
