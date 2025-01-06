package steve6472.moondust.builtin.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.builtin.BuiltinEventCalls;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.blueprint.event.condition.EventCondition;
import steve6472.moondust.widget.blueprint.event.condition.States;
import steve6472.moondust.widget.blueprint.event.condition.Tristate;
import steve6472.moondust.widget.component.flag.Clickable;
import steve6472.moondust.widget.component.CustomData;
import steve6472.moondust.widget.component.event.OnMouseRelease;
import steve6472.moondust.widget.component.event.UIEventCallEntry;
import steve6472.moondust.widget.component.event.UIEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 1/6/2025
 * Project: MoonDust <br>
 */
public record ButtonBlueprint(Key pressCall, String label, boolean labelShadow) implements Blueprint
{
    public static final Key NO_CALL = Key.withNamespace(MoonDustConstants.NAMESPACE, "button/__no_call");
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "button");
    public static final Codec<ButtonBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Key.CODEC.optionalFieldOf("on_press", NO_CALL).forGetter(ButtonBlueprint::pressCall),
        Codec.STRING.optionalFieldOf("label", "").forGetter(ButtonBlueprint::label),
        Codec.BOOL.optionalFieldOf("label_shadow", false).forGetter(ButtonBlueprint::labelShadow)
    ).apply(instance, ButtonBlueprint::new));

    private static final States CONDITION_STATES = new States(Tristate.TRUE, Tristate.TRUE, Tristate.TRUE, Tristate.IGNORE, Tristate.TRUE);

    @Override
    public List<?> createComponents()
    {
        List<UIEventCallEntry> events = new ArrayList<>();
        if (!pressCall.equals(NO_CALL))
            events.add(new UIEventCallEntry(pressCall, new OnMouseRelease(Tristate.IGNORE), new EventCondition(CONDITION_STATES)));

        CustomData data = new CustomData();
        data.putFlag(BuiltinEventCalls.Keys.GENERIC_LABEL_SHADOW, labelShadow);
        if (!label.isBlank()) data.putString(BuiltinEventCalls.Keys.GENERIC_LABEL, label);

        return List.of(new UIEvents(events), Clickable.YES, data);
    }
}
