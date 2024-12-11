package steve6472.moondust.widget.blueprint.event.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.core.event.condition.Tristate;
import steve6472.moondust.widget.blueprint.event.UIEventBlueprint;
import steve6472.moondust.widget.blueprint.event.UIEventType;
import steve6472.moondust.widget.component.event.OnMouseRelease;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record OnMouseReleaseBlueprint(Tristate cursorInside) implements UIEventBlueprint
{
    public static final Codec<OnMouseReleaseBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Tristate.CODEC.optionalFieldOf("cursor_inside", Tristate.IGNORE).forGetter(OnMouseReleaseBlueprint::cursorInside)
    ).apply(instance, OnMouseReleaseBlueprint::new));

    @Override
    public UIEventType<?> getType()
    {
        return UIEventType.ON_MOUSE_RELEASE;
    }

    @Override
    public List<?> createComponents()
    {
        return List.of(new OnMouseRelease(cursorInside));
    }
}
