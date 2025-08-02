package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.core.registry.StringValue;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.FocusedSprite;
import steve6472.moondust.widget.component.RenderOrder;

import java.util.List;

/**
 * Created by steve6472
 * Date: 8/2/2025
 * Project: MoonDust <br>
 */
public record RenderOrderBlueprint(String widget, Order order) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "render_order");
    public static final Codec<RenderOrderBlueprint> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("widget").forGetter(RenderOrderBlueprint::widget),
        Order.CODEC.fieldOf("order").forGetter(RenderOrderBlueprint::order)
    ).apply(instance, RenderOrderBlueprint::new));

    public enum Order implements StringValue
    {
        ABOVE, BELOW;

        public static final Codec<Order> CODEC = StringValue.fromValues(Order::values);

        @Override
        public String stringValue()
        {
            return this == ABOVE ? "above" : "below";
        }
    }
    
    @Override
    public List<?> createComponents()
    {
        return List.of(new RenderOrder(widget, order));
    }
}
