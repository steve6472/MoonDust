package steve6472.moondust.widget.blueprint.flag;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.flag.Visible;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public record VisibleBlueprint(Visible state) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "visible");
    public static final Codec<VisibleBlueprint> CODEC = Codec.BOOL.xmap(b -> new VisibleBlueprint(b ? Visible.YES : Visible.NO), blueprint -> blueprint.state.flag());

    public static final VisibleBlueprint DEFAULT = new VisibleBlueprint(Visible.YES);

    @Override
    public List<?> createComponents()
    {
        return List.of(state);
    }
}
