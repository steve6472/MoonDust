package steve6472.moondust.widget.blueprint.layout;

import com.mojang.serialization.Codec;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.blueprint.position.AbsolutePosBlueprint;
import steve6472.moondust.widget.blueprint.position.AnchoredPosBlueprint;
import steve6472.moondust.widget.blueprint.position.RelativePosBlueprint;
import steve6472.moondust.widget.component.layout.AbsoluteLayout;

import java.util.Collection;
import java.util.List;

/**
 * Created by steve6472
 * Date: 12/7/2024
 * Project: MoonDust <br>
 */
public class AbsoluteLayoutBlueprint implements LayoutBlueprint
{
    public static final AbsoluteLayoutBlueprint INSTANCE = new AbsoluteLayoutBlueprint();
    private static final List<Class<? extends Blueprint>> ACCEPTED_TYPES = List.of(AbsolutePosBlueprint.class, RelativePosBlueprint.class, AnchoredPosBlueprint.class);

    public static final Codec<AbsoluteLayoutBlueprint> CODEC = Codec.unit(INSTANCE);

    private AbsoluteLayoutBlueprint() {}

    @Override
    public LayoutType<?> getType()
    {
        return LayoutType.ABSOLUTE;
    }

    @Override
    public List<?> createComponents()
    {
        return List.of(AbsoluteLayout.INSTANCE);
    }

    @Override
    public Collection<Class<? extends Blueprint>> acceptedPositionTypes()
    {
        return ACCEPTED_TYPES;
    }

    @Override
    public String toString()
    {
        return "AbsoluteLayoutBlueprint{}";
    }
}
