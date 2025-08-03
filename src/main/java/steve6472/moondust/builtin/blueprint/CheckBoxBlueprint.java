package steve6472.moondust.builtin.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.builtin.BuiltinEventCalls;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.view.property.BooleanProperty;
import steve6472.moondust.widget.blueprint.ScriptEntry;
import steve6472.moondust.widget.blueprint.event.condition.EventCondition;
import steve6472.moondust.widget.component.*;
import steve6472.moondust.widget.component.event.OnDataChange;
import steve6472.moondust.widget.component.event.UIEventCallEntry;
import steve6472.moondust.widget.component.event.UIEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by steve6472
 * Date: 1/6/2025
 * Project: MoonDust <br>
 */
public record CheckBoxBlueprint(Key pressCall, Key pressScript, String label, boolean labelShadow, boolean checked) implements Blueprint
{
    public static final Key NO_CALL = Key.withNamespace(MoonDustConstants.NAMESPACE, "checkbox/__no_call");
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "checkbox");
    public static final Codec<CheckBoxBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Key.CODEC.optionalFieldOf("on_press", NO_CALL).forGetter(CheckBoxBlueprint::pressCall),
        Key.CODEC.optionalFieldOf("press_script", NO_CALL).forGetter(CheckBoxBlueprint::pressScript),
        Codec.STRING.optionalFieldOf("label", "").forGetter(CheckBoxBlueprint::label),
        Codec.BOOL.optionalFieldOf("label_shadow", false).forGetter(CheckBoxBlueprint::labelShadow),
        Codec.BOOL.optionalFieldOf("checked", false).forGetter(CheckBoxBlueprint::checked)
    ).apply(instance, CheckBoxBlueprint::new));

    @Override
    public List<?> createComponents()
    {
        List<Object> components = new ArrayList<>();
        List<UIEventCallEntry> events = new ArrayList<>();
        if (!pressCall.equals(NO_CALL))
        {
            events.add(new UIEventCallEntry(pressCall, new OnDataChange(List.of(), List.of(), List.of(), List.of(BuiltinEventCalls.Keys.CHECKBOX_CHECKED)), EventCondition.DEFAULT));
            components.add(new UIEvents(events));
        }

        if (!pressScript.equals(NO_CALL))
        {
            components.add(new Scripts(Map.of("checkbox_press_script", new ScriptEntry(pressScript))));
        }

        CustomData data = new CustomData();
//        data.putFlag(BuiltinEventCalls.Keys.CHECKBOX_CHECKED, checked);

        if (!label.isBlank())
        {
            data.putString(BuiltinEventCalls.Keys.GENERIC_LABEL, label);

            Map<String, Key> stylesMap = new HashMap<>();
            stylesMap.put(BuiltinEventCalls.ID.STYLE_NORMAL, labelShadow ? BuiltinEventCalls.Keys.CHECKBOX_SHADOW_NORMAL : BuiltinEventCalls.Keys.CHECKBOX_NORMAL);
            stylesMap.put(BuiltinEventCalls.ID.STYLE_DISABLED, labelShadow ? BuiltinEventCalls.Keys.CHECKBOX_SHADOW_DISABLED : BuiltinEventCalls.Keys.CHECKBOX_DISABLED);
            stylesMap.put(BuiltinEventCalls.ID.STYLE_HOVER, labelShadow ? BuiltinEventCalls.Keys.CHECKBOX_SHADOW_HOVER : BuiltinEventCalls.Keys.CHECKBOX_HOVER);
            Styles styles = new Styles(stylesMap);
            components.add(styles);
        }

        BooleanProperty checkedProperty = new BooleanProperty(checked);
        checkedProperty.setDebugName("checked");
        Properties properties = new Properties(Map.of("checked", checkedProperty));

        components.add(properties);
        components.add(data);
        components.add(WidgetStates.clickable(true));

        return components;
    }
}
