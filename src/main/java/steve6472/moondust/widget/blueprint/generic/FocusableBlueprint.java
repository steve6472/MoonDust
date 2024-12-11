package steve6472.moondust.widget.blueprint.generic;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.Enabled;
import steve6472.moondust.widget.component.Focusable;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public record FocusableBlueprint(Focusable state) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "focusable");
    public static final Codec<FocusableBlueprint> CODEC = Codec.BOOL.xmap(b -> new FocusableBlueprint(b ? Focusable.YES : Focusable.NO), blueprint -> blueprint.state.flag());

    public static final FocusableBlueprint DEFAULT = new FocusableBlueprint(Focusable.NO);

    @Override
    public List<?> createComponents()
    {
        return List.of(state);
    }
}
