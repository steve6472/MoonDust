package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.child.ChildBlueprints;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.core.blueprint.BlueprintEntry;
import steve6472.moondust.core.blueprint.BlueprintFactory;
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
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "children");
    public static final Key KEY_PANEL = Key.withNamespace(MoonDustConstants.NAMESPACE, "widgets");
    public static final Codec<ChildrenBlueprint> CODEC = MoonDustRegistries.CHILD_BLUEPRINT.valueMapCodec().listOf()
        .xmap(list ->
        {
            List<BlueprintFactory> factories = new ArrayList<>(list.size());

            for (Map<BlueprintEntry<?>, Object> map : list)
            {
                List<Blueprint> blueprints = new ArrayList<>(map.size());

                for (BlueprintEntry<?> blueprintEntry : map.keySet())
                {
                    Object value = map.get(blueprintEntry);

                    if (!(value instanceof Blueprint blueprint))
                    {
                        throw new RuntimeException("Not instance of Blueprint!");
                    }

                    blueprints.add(blueprint);
                }

                for (BlueprintEntry<?> requiredBlueprint : ChildBlueprints.REQUIRED_BLUEPRINTS)
                {
                    if (!map.containsKey(requiredBlueprint))
                    {
                        throw new RuntimeException("Child does not contain required blueprint: '" + requiredBlueprint.key() + "'");
                    }
                }

                BlueprintFactory factory = new BlueprintFactory(Key.withNamespace("unused", "easter_egg"), blueprints);
                factories.add(factory);
            }

            return new ChildrenBlueprint(factories);
        }, children ->
        {
            throw new RuntimeException("Encoding of Children Blueprint is not yet implemented, it was late and I wanted to get other stuff done.");
        });

    @Override
    public List<?> createComponents()
    {
        return List.of(new Children(children));
    }
}
