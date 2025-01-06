package steve6472.moondust;

import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.flare.FlareExport;
import steve6472.moondust.core.MoonDustKeybinds;
import steve6472.moondust.widget.Panel;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.event.OnMouseEnter;
import steve6472.moondust.widget.component.event.OnMouseLeave;
import steve6472.moondust.widget.component.event.OnMousePress;
import steve6472.moondust.widget.component.event.OnMouseRelease;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDust
{
    private static final Logger LOGGER = Log.getLogger(MoonDust.class);
    private static final MoonDust INSTANCE = new MoonDust();

    public static final Key ERROR_FOCUSED = Key.withNamespace("moondust", "widget/error/focused");

    private final List<Panel> panels = new ArrayList<>(2);
    private float pixelScale = 6;
    private Panel focusedPanel;

    public void addPanel(Panel panel)
    {
        panels.add(panel);
    }

    public void removePanel(Panel panel)
    {
        panels.remove(panel);
    }

    /// Returns immutable copy
    public List<Panel> getPanels()
    {
        return List.copyOf(panels);
    }

    public void setPixelScale(float pixelScale)
    {
        this.pixelScale = pixelScale;
    }

    public float getPixelScale()
    {
        return pixelScale;
    }

    public void iterate(Consumer<Widget> widgetConsumer)
    {
        iterate((_, widget) -> widgetConsumer.accept(widget));
    }

    public void iterate(BiConsumer<Integer, Widget> depthWidgetConsumer)
    {
        for (Panel panel : panels)
        {
            iterate(0, panel, depthWidgetConsumer);
        }
    }

    private void iterate(int depth, Widget widget, BiConsumer<Integer, Widget> consumer)
    {
        consumer.accept(depth, widget);

        for (Widget child : widget.getChildren())
        {
            iterate(depth + 1, child, consumer);
        }
    }

    /*
     * Functional
     */

    public void tickFocus()
    {
        if (panels.isEmpty())
            return;

        if (MoonDustKeybinds.NEXT_WIDGET.isActive())
        {
            if (MoonDustKeybinds.BACK_MODIFIER.isActive())
            {
                if (focusedPanel == null)
                    focusedPanel = panels.getLast();
                if (focusedPanel.backwardFocus())
                {
                    focusedPanel.clearFocus();
                    int currentIndex = panels.indexOf(focusedPanel);
                    currentIndex--;
                    if (currentIndex < 0)
                        currentIndex = panels.size() - 1;
                    focusedPanel = panels.get(currentIndex);
                    focusedPanel.backwardFocus();
                }
            } else
            {
                if (focusedPanel == null)
                    focusedPanel = panels.getFirst();
                if (focusedPanel.forwardFocus()) // TODO: probably while ?
                {
                    focusedPanel.clearFocus();
                    int currentIndex = panels.indexOf(focusedPanel);
                    currentIndex++;
                    if (currentIndex >= panels.size())
                        currentIndex = 0;
                    focusedPanel = panels.get(currentIndex);
                    focusedPanel.forwardFocus();
                }
            }
        } else if (MoonDustKeybinds.UNFOCUS_ALL.isActive())
        {
            panels.forEach(Panel::clearFocus);
        } else if (MoonDustKeybinds.CONFIRM.isActive())
        {
            if (focusedPanel == null)
                return;

            focusedPanel.getFocused().ifPresent(widget ->
            {
                widget.internalStates().directHover = true;
                widget.handleEvents(OnMouseEnter.class);
                widget.handleEvents(OnMousePress.class);
                // TODO: Not sure if this is correct 'cause of the tristate ?
                widget.handleEvents(OnMouseRelease.class);
                widget.handleEvents(OnMouseLeave.class);
                widget.internalStates().directHover = false;
            });
        }
    }

    /*
     * Singleton stuff
     */

    private MoonDust() { }

    /// Should be called before Flare.start()
    public void init() throws IOException, URISyntaxException
    {
        FlareExport.exportFolder("moondust/module", MoonDustConstants.MOONDUST_MODULE);
    }

    public static MoonDust getInstance()
    {
        return INSTANCE;
    }
}
