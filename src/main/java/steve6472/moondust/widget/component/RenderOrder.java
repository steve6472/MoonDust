package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.core.registry.StringValue;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.blueprint.RenderOrderBlueprint;

import java.util.List;

/**
 * Created by steve6472
 * Date: 8/2/2025
 * Project: MoonDust <br>
 */
public record RenderOrder(String widget, RenderOrderBlueprint.Order order)
{
    public static final Codec<RenderOrder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("widget").forGetter(RenderOrder::widget),
        RenderOrderBlueprint.Order.CODEC.fieldOf("order").forGetter(RenderOrder::order)
    ).apply(instance, RenderOrder::new));
}
