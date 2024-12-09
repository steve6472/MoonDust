package steve6472.moondust.widget.blueprint.event;

import com.mojang.serialization.Codec;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
public abstract class SimpleCallEventBlueprint implements UIEventBlueprint
{
    private final UIEventType<?> type;
    private final Object eventInstance;

    protected SimpleCallEventBlueprint(UIEventType<?> type, Object eventInstance)
    {
        this.type = type;
        this.eventInstance = eventInstance;
    }

    protected static <T extends SimpleCallEventBlueprint> Codec<T> createCodec(Supplier<T> instance)
    {
        return Codec.unit(instance.get());
    }

    @Override
    public UIEventType<?> getType()
    {
        return type;
    }

    @Override
    public List<?> createComponents()
    {
        return List.of(eventInstance);
    }
}
