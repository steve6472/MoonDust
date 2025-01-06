package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.core.registry.StringValue;
import steve6472.core.util.ExtraCodecs;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.MergeOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 1/6/2025
 * Project: MoonDust <br>
 */
public record MergeOptionsBlueprint(Map<Key, Option> sprites) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "merge_options");
    public static final Codec<MergeOptionsBlueprint> CODEC = ExtraCodecs.mapListCodec(Key.CODEC, Option.CODEC).xmap(MergeOptionsBlueprint::new, MergeOptionsBlueprint::sprites);

    @Override
    public List<?> createComponents()
    {
        return List.of(new MergeOptions(new HashMap<>(sprites)));
    }

    public enum Option implements StringValue
    {
        MERGE,
        REPLACE;

        public static final Codec<Option> CODEC = StringValue.fromValues(Option::values);

        @Override
        public String stringValue()
        {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
