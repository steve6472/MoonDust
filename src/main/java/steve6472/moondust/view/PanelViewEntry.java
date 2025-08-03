package steve6472.moondust.view;

import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;

import java.util.function.Function;

/**
 * Created by steve6472
 * Date: 8/3/2025
 * Project: MoonDust <br>
 */
public record PanelViewEntry(Key key, Function<Key, PanelView> panelViewSupplier) implements Keyable
{
}
