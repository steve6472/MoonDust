package steve6472.moondust.widget;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.DefaultBlueprint;
import steve6472.moondust.widget.blueprint.ChildrenBlueprint;
import steve6472.moondust.widget.blueprint.CurrentSpriteBlueprint;
import steve6472.moondust.widget.blueprint.SpritesBlueprint;
import steve6472.moondust.widget.blueprint.generic.EnabledBlueprint;
import steve6472.moondust.widget.blueprint.generic.VisibleBlueprint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class WidgetBlueprints
{
    public static final List<DefaultBlueprint<?>> DEFAULT_BLUEPRINTS = new ArrayList<>();
    public static final List<BlueprintEntry<?>> REQUIRED_BLUEPRINTS = new ArrayList<>();

    /*
     * Default Blueprints
     * Each component has these even if not defined in file
     */
    public static final BlueprintEntry<EnabledBlueprint> ENABLED = registerDefault(EnabledBlueprint.KEY, EnabledBlueprint.CODEC, EnabledBlueprint.DEFAULT);
    public static final BlueprintEntry<VisibleBlueprint> VISIBLE = registerDefault(VisibleBlueprint.KEY, VisibleBlueprint.CODEC, VisibleBlueprint.DEFAULT);

    /*
     * Normal
     */

    public static final BlueprintEntry<SpritesBlueprint> SPRITES = register(SpritesBlueprint.KEY, SpritesBlueprint.CODEC);
    public static final BlueprintEntry<ChildrenBlueprint> CHILDREN = register(ChildrenBlueprint.KEY, ChildrenBlueprint.CODEC);
    public static final BlueprintEntry<CurrentSpriteBlueprint> CURRENT_SPRITE = register(CurrentSpriteBlueprint.KEY, CurrentSpriteBlueprint.CODEC);

    private static <T extends Blueprint> BlueprintEntry<T> register(Key key, Codec<T> codec)
    {
        if (MoonDustRegistries.WIDGET_BLUEPRINT.get(key) != null)
            throw new RuntimeException("Blueprint with key " + key + " already exists!");

        BlueprintEntry<T> obj = new BlueprintEntry<>(key, codec);
        MoonDustRegistries.WIDGET_BLUEPRINT.register(key, obj);
        return obj;
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerRequired(Key key, Codec<T> codec)
    {
        BlueprintEntry<T> register = register(key, codec);
        REQUIRED_BLUEPRINTS.add(register);
        return register;
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerDefault(Key key, Codec<T> codec, T defaultValue)
    {
        BlueprintEntry<T> register = register(key, codec);
        DEFAULT_BLUEPRINTS.add(new DefaultBlueprint<>(register, defaultValue));
        return register;
    }
}
