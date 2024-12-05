package steve6472.moondust.panel;

import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.widget.Widget;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 *
 */
public class Panel extends Widget
{
    protected Panel(BlueprintFactory blueprint, Widget parent)
    {
        super(blueprint, parent);
    }

    public static Panel create(BlueprintFactory blueprint)
    {
        return new Panel(blueprint, null);
    }

    public static Panel create(Key key)
    {
        return new Panel(MoonDustRegistries.PANEL_FACTORY.get(key), null);
    }
}
