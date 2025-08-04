package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDust;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.view.property.*;
import steve6472.moondust.widget.component.Properties;

import java.util.*;
import java.util.function.Supplier;

/**
 * Created by steve6472
 * Date: 8/3/2025
 * Project: MoonDust <br>
 */
public record PropertiesBlueprint(Map<String, PropertyEntry> properties) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "properties");
    public static final Codec<PropertiesBlueprint> CODEC = Codec.unboundedMap(Codec.STRING, PropertyEntry.CODEC).xmap(PropertiesBlueprint::new, PropertiesBlueprint::properties);

    private static final Set<String> FORBIDDEN_PROPERTY_NAMES = Set.of("enabled", "visible", "focusable", "clickable");
    private static final Map<String, Supplier<Property<?>>> PROPERTIES = new HashMap<>();

    static
    {
        PROPERTIES.put("boolean", BooleanProperty::new);
        PROPERTIES.put("int", IntegerProperty::new);
        PROPERTIES.put("number", NumberProperty::new);
        PROPERTIES.put("string", StringProperty::new);
        PROPERTIES.put("table", TableProperty::new);
    }

    public PropertiesBlueprint
    {
        for (String s : properties.keySet())
        {
            if (FORBIDDEN_PROPERTY_NAMES.contains(s))
                throw new RuntimeException("Tried to defined forbidden property (because each widget has these inheretly and they are set via 'states' blueprint/component : " + s);
        }
    }

    @Override
    public List<?> createComponents()
    {
        Map<String, Property<?>> propertyMap = new HashMap<>();

        properties.forEach((k, v) -> {
            Supplier<Property<?>> propertySupplier = PROPERTIES.get(v.type);
            if (propertySupplier == null)
                throw new RuntimeException("Property with type '%s' not found".formatted(k));
            Property<?> property = propertySupplier.get();
            //noinspection rawtypes,unchecked
            v.defaultValue.ifPresent(val -> ((Property) property).set(val));
            propertyMap.put(k, property);
        });

        return List.of(new Properties(propertyMap));
    }

    private record PropertyEntry(String type, Optional<Object> defaultValue)
    {
        private static final Codec<PropertyEntry> CODEC_DEFAULT = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("type").forGetter(PropertyEntry::type),
            MoonDust.CODEC_LUA_VALUE.optionalFieldOf("default").forGetter(PropertyEntry::defaultValue)
        ).apply(instance, PropertyEntry::new));

        private static final Codec<PropertyEntry> CODEC_INLINE = Codec.STRING.xmap(s -> new PropertyEntry(s, Optional.empty()), p -> p.type);

        public static final Codec<PropertyEntry> CODEC = Codec.withAlternative(CODEC_DEFAULT, CODEC_INLINE);
    }
}
