package steve6472.moondust.panel;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.child.ChildBlueprints;
import steve6472.moondust.child.blueprint.NameBlueprint;
import steve6472.moondust.child.blueprint.SpriteSizeBlueprint;
import steve6472.moondust.child.blueprint.WidgetReferenceBlueprint;
import steve6472.moondust.child.blueprint.position.PositionBlueprint;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.DefaultBlueprint;
import steve6472.moondust.widget.WidgetBlueprints;
import steve6472.moondust.widget.blueprint.ChildrenBlueprint;
import steve6472.moondust.widget.blueprint.generic.EnabledBlueprint;
import steve6472.moondust.widget.blueprint.generic.VisibleBlueprint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public class PanelBlueprints
{
    public static final List<DefaultBlueprint<?>> DEFAULT_BLUEPRINTS = new ArrayList<>();
    public static final List<BlueprintEntry<?>> REQUIRED_BLUEPRINTS = new ArrayList<>();

    // Copied from Widget
    public static final BlueprintEntry<EnabledBlueprint> ENABLED = registerDefault(WidgetBlueprints.ENABLED, EnabledBlueprint.DEFAULT);
    public static final BlueprintEntry<VisibleBlueprint> VISIBLE = registerDefault(WidgetBlueprints.VISIBLE, VisibleBlueprint.DEFAULT);
    public static final BlueprintEntry<ChildrenBlueprint> CHILDREN = registerRequired(ChildrenBlueprint.KEY_PANEL, ChildrenBlueprint.CODEC);

    // Copied from Child
    public static final BlueprintEntry<PositionBlueprint> POSITION = registerRequired(ChildBlueprints.POSITION);
    public static final BlueprintEntry<SpriteSizeBlueprint> SPRITE_SIZE = register(ChildBlueprints.SPRITE_SIZE);

    private static <T extends Blueprint> BlueprintEntry<T> register(Key key, Codec<T> codec)
    {
        if (MoonDustRegistries.PANEL_BLUEPRINT.get(key) != null)
            throw new RuntimeException("Blueprint with key " + key + " already exists!");

        BlueprintEntry<T> obj = new BlueprintEntry<>(key, codec);
        MoonDustRegistries.PANEL_BLUEPRINT.register(key, obj);
        return obj;
    }

    private static <T extends Blueprint> BlueprintEntry<T> register(BlueprintEntry<T> existingEntry)
    {
        if (MoonDustRegistries.PANEL_BLUEPRINT.get(existingEntry.key()) != null)
            throw new RuntimeException("Blueprint with key " + existingEntry.key() + " already exists!");

        MoonDustRegistries.PANEL_BLUEPRINT.register(existingEntry);
        return existingEntry;
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerRequired(Key key, Codec<T> codec)
    {
        BlueprintEntry<T> register = register(key, codec);
        REQUIRED_BLUEPRINTS.add(register);
        return register;
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerRequired(BlueprintEntry<T> existingEntry)
    {
        BlueprintEntry<T> register = register(existingEntry);
        REQUIRED_BLUEPRINTS.add(register);
        return register;
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerDefault(Key key, Codec<T> codec, T defaultValue)
    {
        BlueprintEntry<T> register = register(key, codec);
        DEFAULT_BLUEPRINTS.add(new DefaultBlueprint<>(register, defaultValue));
        return register;
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerDefault(BlueprintEntry<T> existingEntry, T defaultValue)
    {
        BlueprintEntry<T> register = register(existingEntry);
        DEFAULT_BLUEPRINTS.add(new DefaultBlueprint<>(register, defaultValue));
        return register;
    }
}
