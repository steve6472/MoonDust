package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.core.registry.Key;
import steve6472.core.registry.StringValue;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.WidgetStates;

import java.util.List;
import java.util.Locale;

/**
 * Created by steve6472
 * Date: 7/13/2025
 * Project: MoonDust <br>
 */
public record WidgetStatesBl(TriVal enabled, TriVal visible, TriVal clickable, TriVal focusable) implements Blueprint
{
    private enum TriVal
    {
        TRUE, FALSE, INHERIT_PARENT;

        public static final Codec<TriVal> CODEC = Codec.BOOL.xmap(b -> b ? TRUE : FALSE, TriVal::toBool);

        public Boolean toBool()
        {
            if (this == TRUE)
                return true;
            else if (this == FALSE)
                return false;
            else
                return null;
        }
    }

    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "states");
    public static final Codec<WidgetStatesBl> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TriVal.CODEC.optionalFieldOf("enabled", TriVal.INHERIT_PARENT).forGetter(o -> o.enabled),
        TriVal.CODEC.optionalFieldOf("visible", TriVal.INHERIT_PARENT).forGetter(o -> o.visible),
        TriVal.CODEC.optionalFieldOf("clickable", TriVal.INHERIT_PARENT).forGetter(o -> o.clickable),
        TriVal.CODEC.optionalFieldOf("focusable", TriVal.INHERIT_PARENT).forGetter(o -> o.focusable)
    ).apply(instance, WidgetStatesBl::new));

    @Override
    public List<?> createComponents()
    {
        return List.of(new WidgetStates(enabled.toBool(), visible.toBool(), clickable.toBool(), focusable.toBool()));
    }
}
