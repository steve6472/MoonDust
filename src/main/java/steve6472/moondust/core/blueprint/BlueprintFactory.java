package steve6472.moondust.core.blueprint;

import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class BlueprintFactory implements Keyable
{
    private final List<Blueprint> blueprints;
    private final Key key;

    public BlueprintFactory(Key key, List<Blueprint> blueprints)
    {
        this.key = key;
        this.blueprints = blueprints;
    }

    public List<Object> createComponents()
    {
        List<Object> components = new ArrayList<>(blueprints.size());
        blueprints.forEach(blueprint -> components.addAll(blueprint.createComponents()));
        return components;
    }

    public Collection<Blueprint> getBlueprints()
    {
        return List.copyOf(blueprints);
    }

    public <T> T createObject(Function<List<Object>, T> constructor)
    {
        return constructor.apply(createComponents());
    }

    @Override
    public Key key()
    {
        return key;
    }

    @Override
    public String toString()
    {
        return "BlueprintFactory{" + "key=" + key + ", blueprints=" + blueprints + '}';
    }
}
