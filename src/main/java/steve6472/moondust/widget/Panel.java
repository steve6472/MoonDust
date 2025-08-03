package steve6472.moondust.widget;

import steve6472.core.registry.Key;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.view.PanelView;
import steve6472.moondust.widget.component.layout.AbsoluteLayout;
import steve6472.moondust.widget.component.layout.Layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 *
 */
public class Panel extends Widget
{
    private final List<Widget> focusOrder;
    private static final int NO_FOCUS = -1;
    private int currentFocusIndex = NO_FOCUS;

    // TODO: do something with this
    private Layout screenLayout = AbsoluteLayout.INSTANCE;

    protected Panel(BlueprintFactory blueprint, Widget parent)
    {
        super(blueprint, parent);
        List<Widget> focusOrder = new ArrayList<>();
        iterateChildren(widget -> {
            if (widget.isFocusable())
                focusOrder.add(widget);
            return false;
        });
        this.focusOrder = List.copyOf(focusOrder);

    }

    public static Panel create(BlueprintFactory blueprint)
    {
        return new Panel(blueprint, null);
    }

    public static Panel create(Key key)
    {
        return new Panel(MoonDustRegistries.WIDGET_FACTORY.get(key), null);
    }

    public void clearFocus()
    {
        getFocused().ifPresent(widget -> widget.internalStates().focused = false);
        currentFocusIndex = NO_FOCUS;
    }

    public Optional<Widget> getFocused()
    {
        if (currentFocusIndex == NO_FOCUS)
            return Optional.empty();
        return Optional.ofNullable(focusOrder.get(currentFocusIndex));
    }

    /// Return true when this panel ran out of widgets to forward focus to
    public boolean forwardFocus()
    {
        if (focusOrder.isEmpty())
            return true;

        boolean ret = false;

        if (currentFocusIndex != NO_FOCUS)
            focusOrder.get(currentFocusIndex).internalStates().focused = false;
        currentFocusIndex++;
        if (currentFocusIndex >= focusOrder.size())
        {
            currentFocusIndex = 0;
            ret = true;
        }
        focusOrder.get(currentFocusIndex).internalStates().focused = true;
        return ret;
    }

    /// Return true when this panel ran out of widgets to backward focus to
    public boolean backwardFocus()
    {
        if (focusOrder.isEmpty())
            return true;

        boolean ret = false;

        if (currentFocusIndex != NO_FOCUS)
            focusOrder.get(currentFocusIndex).internalStates().focused = false;
        currentFocusIndex--;
        if (currentFocusIndex < 0)
        {
            currentFocusIndex = focusOrder.size() - 1;
            ret = true;
        }
        focusOrder.get(currentFocusIndex).internalStates().focused = true;
        return ret;
    }
}
