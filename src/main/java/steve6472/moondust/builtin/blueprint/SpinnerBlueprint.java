package steve6472.moondust.builtin.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.builtin.BuiltinEventCalls;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.blueprint.event.condition.EventCondition;
import steve6472.moondust.widget.component.CustomData;
import steve6472.moondust.widget.component.Styles;
import steve6472.moondust.widget.component.event.OnDataChange;
import steve6472.moondust.widget.component.event.UIEventCallEntry;
import steve6472.moondust.widget.component.event.UIEvents;
import steve6472.moondust.widget.component.flag.Clickable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 1/25/2025
 * Project: MoonDust <br>
 */
public record SpinnerBlueprint(Key pressCall, String label, boolean labelShadow, NumberFormats numberFormats, Values values) implements Blueprint
{
    public static final Key NO_CALL = Key.withNamespace(MoonDustConstants.NAMESPACE, "spinner/__no_call");
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "spinner");
    public static final Codec<SpinnerBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Key.CODEC.optionalFieldOf("on_press", NO_CALL).forGetter(SpinnerBlueprint::pressCall),
        Codec.STRING.optionalFieldOf("label", "").forGetter(SpinnerBlueprint::label),
        Codec.BOOL.optionalFieldOf("label_shadow", false).forGetter(SpinnerBlueprint::labelShadow),
        NumberFormats.CODEC.optionalFieldOf("number_formats", NumberFormats.DEFAULT).forGetter(SpinnerBlueprint::numberFormats),
        Values.CODEC.optionalFieldOf("values", Values.DEFAULT).forGetter(SpinnerBlueprint::values)
    ).apply(instance, SpinnerBlueprint::new));

    @Override
    public List<?> createComponents()
    {
        List<Object> components = new ArrayList<>();
        List<UIEventCallEntry> events = new ArrayList<>();
        if (!pressCall.equals(NO_CALL))
        {
            events.add(new UIEventCallEntry(pressCall, new OnDataChange(List.of(BuiltinEventCalls.Keys.SPINNER_VALUE), List.of(), List.of(), List.of()), EventCondition.DEFAULT));
            components.add(new UIEvents(events));
        }

        CustomData data = new CustomData();
        data.putFloat(BuiltinEventCalls.Keys.SPINNER_MIN, values.min);
        data.putFloat(BuiltinEventCalls.Keys.SPINNER_MAX, values.max);
        data.putFloat(BuiltinEventCalls.Keys.SPINNER_VALUE, values.value);
        data.putFloat(BuiltinEventCalls.Keys.SPINNER_INCREMENT, values.increment);

        data.putString(BuiltinEventCalls.Keys.GENERIC_LABEL, label);
        data.putString(BuiltinEventCalls.Keys.SPINNER_NUMBER_FORMAT_MIN, numberFormats.min);
        data.putString(BuiltinEventCalls.Keys.SPINNER_NUMBER_FORMAT_MAX, numberFormats.max);
        data.putString(BuiltinEventCalls.Keys.SPINNER_NUMBER_FORMAT_VALUE, numberFormats.value);
        data.putString(BuiltinEventCalls.Keys.SPINNER_NUMBER_FORMAT_INCREMENT, numberFormats.increment);

        Map<String, Key> stylesMap = new HashMap<>();
        stylesMap.put(BuiltinEventCalls.ID.STYLE_NORMAL, labelShadow ? BuiltinEventCalls.Keys.SPINNER_SHADOW_NORMAL : BuiltinEventCalls.Keys.SPINNER_NORMAL);
        stylesMap.put(BuiltinEventCalls.ID.STYLE_DISABLED, labelShadow ? BuiltinEventCalls.Keys.SPINNER_SHADOW_DISABLED : BuiltinEventCalls.Keys.SPINNER_DISABLED);
        Styles styles = new Styles(stylesMap);
        components.add(styles);

        components.add(data);

        return components;
    }

    private record NumberFormats(String value, String min, String max, String increment)
    {
        NumberFormats(String format)
        {
            this(format, format, format, format);
        }

        private static final Codec<NumberFormats> CODEC_SEPARATE = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("value", BuiltinEventCalls.DEFAULT_NUMBER_FORMAT).forGetter(NumberFormats::value),
            Codec.STRING.optionalFieldOf("min", BuiltinEventCalls.DEFAULT_NUMBER_FORMAT).forGetter(NumberFormats::min),
            Codec.STRING.optionalFieldOf("max", BuiltinEventCalls.DEFAULT_NUMBER_FORMAT).forGetter(NumberFormats::max),
            Codec.STRING.optionalFieldOf("increment", BuiltinEventCalls.DEFAULT_NUMBER_FORMAT).forGetter(NumberFormats::increment)
        ).apply(instance, NumberFormats::new));

        private static final Codec<NumberFormats> CODEC = Codec.withAlternative(CODEC_SEPARATE, Codec.STRING, NumberFormats::new);

        private static final NumberFormats DEFAULT = new NumberFormats(BuiltinEventCalls.DEFAULT_NUMBER_FORMAT);
    }

    private record Values(float min, float max, float value, float increment)
    {
        private static final float DEFAULT_MIN_VALUE = 0f;
        private static final float DEFAULT_MAX_VALUE = Float.MAX_VALUE;
        private static final float DEFAULT_VALUE = 0f;
        private static final float DEFAULT_INCREMENT = 1f;

        public static final Codec<Values> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("min", DEFAULT_MIN_VALUE).forGetter(Values::min),
            Codec.FLOAT.optionalFieldOf("max", DEFAULT_MAX_VALUE).forGetter(Values::max),
            Codec.FLOAT.optionalFieldOf("value", DEFAULT_VALUE).forGetter(Values::value),
            Codec.FLOAT.optionalFieldOf("increment", DEFAULT_INCREMENT).forGetter(Values::increment)
        ).apply(instance, Values::new));

        private static final Values DEFAULT = new Values(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, DEFAULT_VALUE, DEFAULT_INCREMENT);
    }
}
