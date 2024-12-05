package steve6472.moondust.child;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.child.blueprint.NameBlueprint;
import steve6472.moondust.child.blueprint.WidgetReferenceBlueprint;
import steve6472.moondust.child.blueprint.position.PositionBlueprint;
import steve6472.moondust.core.MoonDustComponentRegister;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.DefaultBlueprint;
import steve6472.moondust.widget.WidgetBlueprints;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public class ChildBlueprints
{
    public static final List<DefaultBlueprint<?>> DEFAULT_BLUEPRINTS = new ArrayList<>();
    public static final List<BlueprintEntry<?>> REQUIRED_BLUEPRINTS = new ArrayList<>();

    static
    {
        final Set<Key> excluded = Set.of(
            // Children would just allow for really complicated definitions, not what I want
            WidgetBlueprints.CHILDREN.key()
        );

        // Copy blueprints from Widgets to Child
        for (Map.Entry<Key, BlueprintEntry<?>> entry : MoonDustRegistries.WIDGET_BLUEPRINT.getMap().entrySet())
        {
            if (!excluded.contains(entry.getKey()))
                MoonDustRegistries.CHILD_BLUEPRINT.register(entry.getKey(), entry.getValue());
        }
        DEFAULT_BLUEPRINTS.addAll(WidgetBlueprints.DEFAULT_BLUEPRINTS);
    }

    public static final BlueprintEntry<WidgetReferenceBlueprint> WIDGET = registerRequired(WidgetReferenceBlueprint.KEY, WidgetReferenceBlueprint.CODEC);
    public static final BlueprintEntry<NameBlueprint> NAME = registerRequired(NameBlueprint.KEY, NameBlueprint.CODEC);
    public static final BlueprintEntry<PositionBlueprint> POSITION = registerRequired(PositionBlueprint.KEY, PositionBlueprint.CODEC);

    /*
     * Register methods
     */

    private static <T extends Blueprint> BlueprintEntry<T> register(Key key, Codec<T> codec)
    {
        return MoonDustComponentRegister.register(MoonDustRegistries.CHILD_BLUEPRINT, key, codec);
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerRequired(Key key, Codec<T> codec)
    {
        return MoonDustComponentRegister.registerRequired(MoonDustRegistries.CHILD_BLUEPRINT, REQUIRED_BLUEPRINTS, key, codec);
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerDefault(Key key, Codec<T> codec, T defaultValue)
    {
        return MoonDustComponentRegister.registerDefault(MoonDustRegistries.CHILD_BLUEPRINT, DEFAULT_BLUEPRINTS, key, codec, defaultValue);
    }
}
