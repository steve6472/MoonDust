package steve6472.moondust.widget.blueprint.flag;

import com.mojang.serialization.Codec;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.flag.Clickable;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public record ClickableBlueprint(Clickable state) implements Blueprint
{
    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "clickable");
    public static final Codec<ClickableBlueprint> CODEC = Codec.BOOL.xmap(b -> new ClickableBlueprint(b ? Clickable.YES : Clickable.NO), blueprint -> blueprint.state.flag());

    public static final ClickableBlueprint DEFAULT = new ClickableBlueprint(Clickable.NO);

    @Override
    public List<?> createComponents()
    {
        return List.of(state);
    }
}
