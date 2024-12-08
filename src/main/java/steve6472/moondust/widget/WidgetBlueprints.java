package steve6472.moondust.widget;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.widget.blueprint.NameBlueprint;
import steve6472.moondust.widget.blueprint.WidgetReferenceBlueprint;
import steve6472.moondust.widget.blueprint.position.PositionBlueprint;
import steve6472.moondust.widget.blueprint.SpriteSizeBlueprint;
import steve6472.moondust.core.MoonDustComponentRegister;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.DefaultBlueprint;
import steve6472.moondust.widget.blueprint.BoundsBlueprint;
import steve6472.moondust.widget.blueprint.ChildrenBlueprint;
import steve6472.moondust.widget.blueprint.CurrentSpriteBlueprint;
import steve6472.moondust.widget.blueprint.SpritesBlueprint;
import steve6472.moondust.widget.blueprint.generic.EnabledBlueprint;
import steve6472.moondust.widget.blueprint.generic.VisibleBlueprint;
import steve6472.moondust.widget.blueprint.layout.LayoutBlueprint;

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
    public static final BlueprintEntry<LayoutBlueprint> LAYOUT = registerDefault(LayoutBlueprint.KEY, LayoutBlueprint.CODEC, LayoutBlueprint.DEFAULT);

    /*
     * Normal
     */

    public static final BlueprintEntry<SpritesBlueprint> SPRITES = register(SpritesBlueprint.KEY, SpritesBlueprint.CODEC);
    public static final BlueprintEntry<CurrentSpriteBlueprint> CURRENT_SPRITE = register(CurrentSpriteBlueprint.KEY, CurrentSpriteBlueprint.CODEC);
    public static final BlueprintEntry<SpriteSizeBlueprint> SPRITE_SIZE = register(SpriteSizeBlueprint.KEY, SpriteSizeBlueprint.CODEC);
    public static final BlueprintEntry<ChildrenBlueprint> CHILDREN = register(ChildrenBlueprint.KEY, ChildrenBlueprint.CODEC);
    public static final BlueprintEntry<BoundsBlueprint> BOUNDS = register(BoundsBlueprint.KEY, BoundsBlueprint.CODEC);

    public static final BlueprintEntry<WidgetReferenceBlueprint> WIDGET = register(WidgetReferenceBlueprint.KEY, WidgetReferenceBlueprint.CODEC);
    public static final BlueprintEntry<NameBlueprint> NAME = register(NameBlueprint.KEY, NameBlueprint.CODEC);
    public static final BlueprintEntry<PositionBlueprint> POSITION = register(PositionBlueprint.KEY, PositionBlueprint.CODEC);

    /*
     * Register methods
     */

    private static <T extends Blueprint> BlueprintEntry<T> register(Key key, Codec<T> codec)
    {
        return MoonDustComponentRegister.register(MoonDustRegistries.WIDGET_BLUEPRINT, key, codec);
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerRequired(Key key, Codec<T> codec)
    {
        return MoonDustComponentRegister.registerRequired(MoonDustRegistries.WIDGET_BLUEPRINT, REQUIRED_BLUEPRINTS, key, codec);
    }

    private static <T extends Blueprint> BlueprintEntry<T> registerDefault(Key key, Codec<T> codec, T defaultValue)
    {
        return MoonDustComponentRegister.registerDefault(MoonDustRegistries.WIDGET_BLUEPRINT, DEFAULT_BLUEPRINTS, key, codec, defaultValue);
    }
}
