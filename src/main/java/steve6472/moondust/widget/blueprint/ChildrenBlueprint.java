package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.widget.WidgetLoader;
import steve6472.moondust.widget.component.Children;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public record ChildrenBlueprint(List<BlueprintFactory> children) implements Blueprint
{
    private static final Key KEY_CHILD_COMPONENT = Key.withNamespace("moondust", "__temp_child_component");

    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "children");
    public static final Codec<ChildrenBlueprint> CODEC = MoonDustRegistries.WIDGET_BLUEPRINT.valueMapCodec().listOf()
        .xmap(list ->
        {
            List<BlueprintFactory> factories = new ArrayList<>(list.size());

            for (Map<BlueprintEntry<?>, Object> map : list)
            {
                factories.add(WidgetLoader.createWidgetFactory(map, KEY_CHILD_COMPONENT));
            }

            return new ChildrenBlueprint(factories);
        }, _ ->
        {
            throw new RuntimeException("Encoding of Children Blueprint is not yet implemented, it was late and I wanted to get other stuff done.");
        });

    @Override
    public List<?> createComponents()
    {
        return List.of(new Children(children));
    }
}
