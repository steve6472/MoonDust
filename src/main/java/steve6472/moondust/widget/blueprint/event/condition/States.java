package steve6472.moondust.widget.blueprint.event.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.InternalStates;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
public record States(Tristate enabled, Tristate visible, Tristate clickable, Tristate focused, Tristate directHover) implements Condition
{
    public static final Codec<States> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Tristate.CODEC.optionalFieldOf("enabled", Tristate.IGNORE).forGetter(States::enabled),
        Tristate.CODEC.optionalFieldOf("visible", Tristate.IGNORE).forGetter(States::visible),
        Tristate.CODEC.optionalFieldOf("clickable", Tristate.IGNORE).forGetter(States::clickable),
        Tristate.CODEC.optionalFieldOf("focused", Tristate.IGNORE).forGetter(States::focused),
        Tristate.CODEC.optionalFieldOf("direct_hover", Tristate.IGNORE).forGetter(States::directHover)
    ).apply(instance, States::new));

    public static final States DEFAULT = new States(Tristate.IGNORE, Tristate.IGNORE, Tristate.IGNORE, Tristate.IGNORE ,Tristate.IGNORE);

    @Override
    public boolean test(Widget widget)
    {
        InternalStates internal = widget.internalStates();

        return
            enabled.test(widget.isEnabled()) &&
            visible.test(widget.isVisible()) &&
            clickable.test(widget.isClickable()) &&
            focused.test(internal.focused) &&
            directHover.test(internal.directHover);
    }
}
