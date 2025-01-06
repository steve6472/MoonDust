package steve6472.moondust.core.blueprint;

import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class BlueprintFactory implements Keyable
{
    private final Map<Key, Blueprint> blueprints;
    private final Key key;

    public BlueprintFactory(Key key, Map<Key, Blueprint> blueprints)
    {
        this.key = key;
        this.blueprints = blueprints;
    }

    public List<Object> createComponents()
    {
        List<Object> components = new ArrayList<>(blueprints.size());
        blueprints.values().forEach(blueprint -> components.addAll(blueprint.createComponents()));
        return components;
    }

    public Map<Key, Blueprint> blueprints()
    {
        return new LinkedHashMap<>(blueprints);
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
