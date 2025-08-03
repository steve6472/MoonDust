package steve6472.moondust.widget.blueprint;

import com.mojang.serialization.Codec;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.view.PanelView;
import steve6472.moondust.view.PanelViewEntry;
import steve6472.moondust.widget.component.ViewController;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public record ViewControllerBlueprint(Key key) implements Blueprint
{
    private static final Logger LOGGER = Log.getLogger(ViewControllerBlueprint.class);

    public static final Key KEY = Key.withNamespace(MoonDustConstants.NAMESPACE, "view_controller");
    public static final Codec<ViewControllerBlueprint> CODEC = Key.CODEC.xmap(ViewControllerBlueprint::new, ViewControllerBlueprint::key);

    @Override
    public List<?> createComponents()
    {
        PanelViewEntry panelViewEntry = MoonDustRegistries.PANEL_VIEW.get(key);
        if (panelViewEntry == null)
        {
            LOGGER.severe("View Controller '%s' not found".formatted(key));
            return List.of();
        }
        PanelView panelView = panelViewEntry.panelViewSupplier().apply(key);
        return List.of(new ViewController(key, panelView));
    }
}
