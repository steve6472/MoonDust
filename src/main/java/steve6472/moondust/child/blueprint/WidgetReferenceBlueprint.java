package steve6472.moondust.child.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.child.component.WidgetReference;
import steve6472.moondust.core.blueprint.Blueprint;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public record WidgetReferenceBlueprint(Key widget) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "widget");
    public static final Codec<WidgetReferenceBlueprint> CODEC = Key.CODEC.xmap(WidgetReferenceBlueprint::new, WidgetReferenceBlueprint::widget);

    @Override
    public List<?> createComponents()
    {
        return List.of(new WidgetReference(widget));
    }
}
