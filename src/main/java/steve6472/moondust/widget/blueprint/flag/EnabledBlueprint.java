package steve6472.moondust.widget.blueprint.flag;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.flag.Enabled;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public record EnabledBlueprint(Enabled state) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "enabled");
    public static final Codec<EnabledBlueprint> CODEC = Codec.BOOL.xmap(b -> new EnabledBlueprint(b ? Enabled.YES : Enabled.NO), blueprint -> blueprint.state.flag());

    public static final EnabledBlueprint DEFAULT = new EnabledBlueprint(Enabled.YES);

    @Override
    public List<?> createComponents()
    {
        return List.of(state);
    }
}
