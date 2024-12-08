package steve6472.moondust.widget;

import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.widget.blueprint.layout.LayoutType;
import steve6472.moondust.widget.component.layout.AbsoluteLayout;
import steve6472.moondust.widget.component.layout.Layout;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 *
 */
public class Panel extends Widget
{
    // TODO: do something with this
    private Layout screenLayout = AbsoluteLayout.INSTANCE;

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
        return new Panel(MoonDustRegistries.WIDGET_FACTORY.get(key), null);
    }
}
