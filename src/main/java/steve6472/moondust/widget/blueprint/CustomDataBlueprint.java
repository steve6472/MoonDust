package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.CustomData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/18/2024
 * Project: MoonDust <br>
 */
public record CustomDataBlueprint(Map<Key, String> strings, Map<Key, Float> floats, Map<Key, Integer> ints, Map<Key, Boolean> flags) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "data");

    public static final Codec<CustomDataBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.mapListCodec(Key.CODEC, Codec.STRING).optionalFieldOf("strings", new HashMap<>()).forGetter(CustomDataBlueprint::strings),
        ExtraCodecs.mapListCodec(Key.CODEC, Codec.FLOAT).optionalFieldOf("floats", new HashMap<>()).forGetter(CustomDataBlueprint::floats),
        ExtraCodecs.mapListCodec(Key.CODEC, Codec.INT).optionalFieldOf("ints", new HashMap<>()).forGetter(CustomDataBlueprint::ints),
        ExtraCodecs.mapListCodec(Key.CODEC, Codec.BOOL).optionalFieldOf("flags", new HashMap<>()).forGetter(CustomDataBlueprint::flags)
    ).apply(instance, CustomDataBlueprint::new));

    @Override
    public List<?> createComponents()
    {
        CustomData data = new CustomData();
        strings.forEach(data::putString);
        floats.forEach(data::putFloat);
        ints.forEach(data::putInt);
        flags.forEach(data::putFlag);
        return List.of(data);
    }
}
