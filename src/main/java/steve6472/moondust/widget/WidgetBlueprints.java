package steve6472.moondust.widget;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.widget.blueprint.*;
import steve6472.moondust.widget.blueprint.event.EventsBlueprint;
import steve6472.moondust.widget.blueprint.flag.ClickableBlueprint;
import steve6472.moondust.widget.blueprint.flag.FocusableBlueprint;
import steve6472.moondust.widget.blueprint.position.PositionBlueprint;
import steve6472.moondust.core.MoonDustComponentRegister;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.DefaultBlueprint;
import steve6472.moondust.widget.blueprint.flag.EnabledBlueprint;
import steve6472.moondust.widget.blueprint.flag.VisibleBlueprint;
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

    /*
     * Default Blueprints
     * Each component has these even if not defined in file
     */
    public static final BlueprintEntry<EnabledBlueprint> ENABLED = registerDefault(EnabledBlueprint.KEY, EnabledBlueprint.CODEC, EnabledBlueprint.DEFAULT);
    public static final BlueprintEntry<VisibleBlueprint> VISIBLE = registerDefault(VisibleBlueprint.KEY, VisibleBlueprint.CODEC, VisibleBlueprint.DEFAULT);
    public static final BlueprintEntry<ClickableBlueprint> CLICKABLE = registerDefault(ClickableBlueprint.KEY, ClickableBlueprint.CODEC, ClickableBlueprint.DEFAULT);
    public static final BlueprintEntry<FocusableBlueprint> FOCUSABLE = registerDefault(FocusableBlueprint.KEY, FocusableBlueprint.CODEC, FocusableBlueprint.DEFAULT);
    public static final BlueprintEntry<LayoutBlueprint> LAYOUT = registerDefault(LayoutBlueprint.KEY, LayoutBlueprint.CODEC, LayoutBlueprint.DEFAULT);

    /*
     * Normal
     */

    // Sprites/Rendering
    public static final BlueprintEntry<SpritesBlueprint> SPRITES = register(SpritesBlueprint.KEY, SpritesBlueprint.CODEC);
    public static final BlueprintEntry<CurrentSpriteBlueprint> CURRENT_SPRITE = register(CurrentSpriteBlueprint.KEY, CurrentSpriteBlueprint.CODEC);
    public static final BlueprintEntry<SpriteSizeBlueprint> SPRITE_SIZE = register(SpriteSizeBlueprint.KEY, SpriteSizeBlueprint.CODEC);
    public static final BlueprintEntry<SpriteOffsetBlueprint> SPRITE_OFFSET = register(SpriteOffsetBlueprint.KEY, SpriteOffsetBlueprint.CODEC);
    public static final BlueprintEntry<FocusedSpriteBlueprint> FOCUSED_SPRITE = register(FocusedSpriteBlueprint.KEY, FocusedSpriteBlueprint.CODEC);
    public static final BlueprintEntry<TextBlueprint> TEXT_LINE = register(TextBlueprint.KEY, TextBlueprint.CODEC);
    public static final BlueprintEntry<ZIndexBlueprint> Z_INDEX = register(ZIndexBlueprint.KEY, ZIndexBlueprint.CODEC);
    public static final BlueprintEntry<StylesBlueprint> STYLES = register(StylesBlueprint.KEY, StylesBlueprint.CODEC);

    // Clickbox
    public static final BlueprintEntry<ClickboxSizeBlueprint> CLICKBOX_SIZE = register(ClickboxSizeBlueprint.KEY, ClickboxSizeBlueprint.CODEC);
    public static final BlueprintEntry<ClickboxOffsetBlueprint> CLICKBOX_OFFSET = register(ClickboxOffsetBlueprint.KEY, ClickboxOffsetBlueprint.CODEC);

    public static final BlueprintEntry<ChildrenBlueprint> CHILDREN = register(ChildrenBlueprint.KEY, ChildrenBlueprint.CODEC);
    public static final BlueprintEntry<BoundsBlueprint> BOUNDS = register(BoundsBlueprint.KEY, BoundsBlueprint.CODEC);
    @Deprecated(forRemoval = true)
    public static final BlueprintEntry<EventsBlueprint> EVENTS = register(EventsBlueprint.KEY, EventsBlueprint.CODEC);
    public static final BlueprintEntry<ScriptsBlueprint> SCRIPTS = register(ScriptsBlueprint.KEY, ScriptsBlueprint.CODEC);
    public static final BlueprintEntry<OverridesBlueprint> OVERRIDES = register(OverridesBlueprint.KEY, OverridesBlueprint.CODEC);
    public static final BlueprintEntry<CustomDataBlueprint> DATA = register(CustomDataBlueprint.KEY, CustomDataBlueprint.CODEC);
    public static final BlueprintEntry<RadioGroupBlueprint> RADIO_GROUP = register(RadioGroupBlueprint.KEY, RadioGroupBlueprint.CODEC);

    // Children specific
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

    private static <T extends Blueprint> BlueprintEntry<T> registerDefault(Key key, Codec<T> codec, T defaultValue)
    {
        return MoonDustComponentRegister.registerDefault(MoonDustRegistries.WIDGET_BLUEPRINT, DEFAULT_BLUEPRINTS, key, codec, defaultValue);
    }
}
