package steve6472.moondust;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import org.joml.Vector2i;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.core.util.JarExport;
import steve6472.flare.Window;
import steve6472.moondust.core.MoonDustKeybinds;
import steve6472.moondust.widget.Panel;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.ClickboxOffset;
import steve6472.moondust.widget.component.event.*;
import steve6472.moondust.widget.component.event.global.OnGlobalPixelScaleChange;
import steve6472.radiant.LuaTableOps;
import steve6472.radiant.LuauTable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    /* # TODOS:

     * [X] Blueprints:
     * - each blueprint is defined by file name
     * - 2 types
     *   - single inline value, blueprint contains the "value" field (limited_int)
     *   - multiple fields, contains the "fields" field (slider)
     * - the value in either "value" or "fields" is the same type, typed object
     * - "fields" is an unbout map of string and value
     * - Have to load BEFORE widgets.. ofc
     *
     * [X] Blueprints verifier:
     * - each custom blueprint has to be verified when read from json, remember to verify the min/max limits too
     *
     * [X] Move clickable, enabled, visible, ??? to state variable in Widget instead each being a component, have blueprint to create this tho, special just like custom data
     *
     * [X] WidgetLoader L95 - sprite_size should work same as clickbox, if sprite size is not present use bounds
     *
     * [X] CustomData Struct
     * - Just a Map of <String, Object>
     * - Idea is that this is settable from Lua (from custom blueprint for example)
     * - Basically just a custom component
     *
     * [ ] All components that are NOT used to render or are used so interaction is possible should be replaced with CustomData structs and luaified
     *
     *
     * [X] Scripts need arguments
     * - Use custom blueprints for script input lol
     * - If a script has input it should be specified by the script returning a table which is the custom blueprint.
     * - This ended up being done completely differently lol, no verification 'cause I was lazy. Maybe one day (but probably not)
     *
     * [ ] Expand custom blueprints into custom components, with full field checking
     *  - [ ] Change validation result fix to include any value
     *
     * [ ] Canvas - abilty to render arbitary graphics
     *
     * [x] Bridge parameters - changing value in lua should reflect value change in java and vice versa
     *
     * [ ] Consider similar system to Bounds % size but with position?
     *
     * [ ] Font render order (take inspiration from MoonDustUIRender sortAndRenderPrimitives)
     *
     * Bugs:
     *  [ ] Tab navigation after changing panels does not work
     *  [ ] Font Rendering is relying on a hack
     */
    private static final Logger LOGGER = Log.getLogger(MoonDust.class);
    private static final MoonDust INSTANCE = new MoonDust();
    private static final int FOCUS_ITERATION_LIMIT = 1024;

    public static final Key ERROR_FOCUSED = Key.withNamespace("moondust", "widget/error/focused");

    private final List<Panel> panels = new ArrayList<>(2);
    private Window window;
    private float pixelScale = 4;
    private Panel focusedPanel;

    public static final Codec<LuauTable> CODEC_TABLE = Codec.PASSTHROUGH.flatXmap(dyn -> {
        Object value = dyn.convert(LuaTableOps.INSTANCE).getValue();
        if (!(value instanceof LuauTable table))
            return DataResult.error(() -> "Not a lua table!");
        return DataResult.success(table);
    }, table -> DataResult.error(() -> "Do not know how to serialize " + table));

    public static final Codec<Object> CODEC_LUA_VALUE = Codec.PASSTHROUGH.flatXmap(dyn -> {
        Object value = dyn.convert(LuaTableOps.INSTANCE).getValue();
        return DataResult.success(value);
    }, object -> DataResult.success(new Dynamic<>(LuaTableOps.INSTANCE, object)));

    public void setWindow(Window window)
    {
        this.window = window;
    }

    public Window window()
    {
        return window;
    }

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

        iterate((depth, widget) ->
        {
            if (depth == 0 && widget instanceof Panel panel)
            {
                panel.setBounds((int) (window.getWidth() / pixelScale), (int) (window.getHeight() / pixelScale));
            }
        });
        runGlobalEvent(new OnGlobalPixelScaleChange((int) pixelScale));
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

    public <T extends UIEvent> void runGlobalEvent(T event)
    {
        //noinspection unchecked
        Class<T> aClass = (Class<T>) event.getClass();
        iterate(widget -> widget.handleEvents(aClass, _ -> true, event));
    }

    public Optional<Widget> getTopWidgetAt(int x, int y)
    {
        Widget[] ret = {null};

        MoonDust moonDust = MoonDust.getInstance();
        float pixelScale = moonDust.getPixelScale();

        int mx = x / (int) pixelScale;
        int my = y / (int) pixelScale;

        iterate((depth, widget) ->
        {
            if (!widget.isVisible() || !widget.isClickable())
                return;

            widget.getClickboxSize().ifPresent(clickboxSize ->
            {
                Vector2i clickboxPosition = widget.getPosition();
                widget.getComponent(ClickboxOffset.class).ifPresent(offset -> clickboxPosition.add(offset.x, offset.y));
                boolean hovered = isInRectangle(clickboxPosition.x, clickboxPosition.y, clickboxPosition.x + clickboxSize.width(), clickboxPosition.y + clickboxSize.height(), mx, my);

                if (hovered)
                    ret[0] = widget;
            });
        });

        return Optional.ofNullable(ret[0]);
    }

    private static boolean isInRectangle(int rminx, int rminy, int rmaxx, int rmaxy, int px, int py)
    {
        return px >= rminx && px < rmaxx && py >= rminy && py < rmaxy;
    }

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
            clearFocus();
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

    public void clearFocus()
    {
        panels.forEach(Panel::clearFocus);
    }

    public void focus(Widget widget)
    {
        if (!widget.isFocusable())
        {
            LOGGER.warning("Tried to grab focus on unfocusable widget " + widget.getPath());
            return;
        }

        clearFocus();
        Optional<Widget> parent = widget.parent();

        final int[] iterationsLimit = {0};
        while (parent.isPresent() && !(parent.get() instanceof Panel))
        {
            if (iterationsLimit[0] > FOCUS_ITERATION_LIMIT)
            {
                LOGGER.severe("Could not focus a widget %s, went into infinite while loop".formatted(widget.getPath()));
                return;
            }
            parent = parent.get().parent();
            iterationsLimit[0]++;
        }

        iterationsLimit[0] = 0;
        parent.ifPresent(wgt -> {
            Panel parentPanel = (Panel) wgt;

            while (parentPanel.getFocused().orElse(null) != widget)
            {
                if (iterationsLimit[0] > FOCUS_ITERATION_LIMIT)
                {
                    LOGGER.severe("Could not focus a widget %s, went into infinite while loop".formatted(widget.getPath()));
                    return;
                }
                parentPanel.forwardFocus();
                iterationsLimit[0]++;
            }
            focusedPanel = parentPanel;
        });
    }

    public Optional<Widget> getFocused()
    {
        if (focusedPanel == null)
            return Optional.empty();

        return focusedPanel.getFocused();
    }

    /*
     * Singleton stuff
     */

    private MoonDust() { }

    /// Should be called before Flare.start()
    public void init() throws IOException, URISyntaxException
    {
        JarExport.exportFolder("moondust/module", MoonDustConstants.MOONDUST_MODULE);
    }

    public static MoonDust getInstance()
    {
        return INSTANCE;
    }
}
