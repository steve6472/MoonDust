package steve6472.moondust.widget.blueprint.layout;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Typed;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.layout.AbsoluteLayout;

import java.util.Collection;

/**
 * Created by steve6472
 * Date: 12/7/2024
 * Project: MoonDust <br>
 */
public interface LayoutBlueprint extends Typed<LayoutType<?>>, Blueprint
{
    Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "layout");
    Codec<LayoutBlueprint> CODEC = MoonDustRegistries.LAYOUT_TYPE.byKeyCodec().dispatch("type", LayoutBlueprint::getType, LayoutType::mapCodec);
    LayoutBlueprint DEFAULT = AbsoluteLayoutBlueprint.INSTANCE;

    Collection<Class<? extends Blueprint>> acceptedPositionTypes();
}
