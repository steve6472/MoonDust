package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.Mergeable;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.widget.WidgetLoader;
import steve6472.moondust.widget.blueprint.ChildrenBlueprint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 */
public record Children(List<BlueprintFactory> children) implements Mergeable<Children>
{
    private static final Key KEY_CHILD_COMPONENT = Key.withNamespace(MoonDustConstants.NAMESPACE, "__temp_child_component2");

    public static final Codec<Children> CODEC = MoonDustRegistries.WIDGET_BLUEPRINT.valueMapCodec().listOf()
        .xmap(list ->
        {
            List<BlueprintFactory> factories = new ArrayList<>(list.size());

            for (Map<BlueprintEntry<?>, Object> map : list)
            {
                factories.add(WidgetLoader.createWidgetFactory(map, KEY_CHILD_COMPONENT));
            }

            return new Children(factories);
        }, _ ->
        {
            throw new RuntimeException("Encoding of Children Blueprint is not yet implemented, it was late and I wanted to get other stuff done.");
        });

    @Override
    public Children merge(Children left, Children right)
    {
        Children children = new Children(new ArrayList<>(left.children));
        children.children.addAll(right.children);
        return children;
    }
}
