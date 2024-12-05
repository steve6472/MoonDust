package steve6472.moondust.panel;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.child.ChildBlueprints;
import steve6472.moondust.core.MoonDustComponentRegister;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.DefaultBlueprint;
import steve6472.moondust.widget.WidgetBlueprints;
import steve6472.moondust.widget.blueprint.ChildrenBlueprint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public class PanelBlueprints
{
    public static final List<DefaultBlueprint<?>> DEFAULT_BLUEPRINTS = new ArrayList<>();
    public static final List<BlueprintEntry<?>> REQUIRED_BLUEPRINTS = new ArrayList<>();

    static
    {
        final Set<Key> excluded = Set.of(
            // Children component has to be renamed to "widgets"
            WidgetBlueprints.CHILDREN.key(),
            ChildBlueprints.WIDGET.key(),
            ChildBlueprints.NAME.key()
        );

        // Copy blueprints from Child to Panel
        for (Map.Entry<Key, BlueprintEntry<?>> entry : MoonDustRegistries.CHILD_BLUEPRINT.getMap().entrySet())
        {
            if (!excluded.contains(entry.getKey()))
                MoonDustRegistries.PANEL_BLUEPRINT.register(entry.getKey(), entry.getValue());
        }
        DEFAULT_BLUEPRINTS.addAll(ChildBlueprints.DEFAULT_BLUEPRINTS);
    }

    public static final BlueprintEntry<ChildrenBlueprint> CHILDREN = registerRequired(ChildrenBlueprint.KEY_PANEL, ChildrenBlueprint.CODEC);

    /*
     * Register methods
     */

    private static <T extends Blueprint> BlueprintEntry<T> register(Key key, Codec<T> codec)
    {
        return MoonDustComponentRegister.register(MoonDustRegistries.PANEL_BLUEPRINT, key, codec);
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerRequired(Key key, Codec<T> codec)
    {
        return MoonDustComponentRegister.registerRequired(MoonDustRegistries.PANEL_BLUEPRINT, REQUIRED_BLUEPRINTS, key, codec);
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerDefault(Key key, Codec<T> codec, T defaultValue)
    {
        return MoonDustComponentRegister.registerDefault(MoonDustRegistries.PANEL_BLUEPRINT, DEFAULT_BLUEPRINTS, key, codec, defaultValue);
    }
}
