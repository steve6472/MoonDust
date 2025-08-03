package steve6472.moondust.view;

import com.mojang.datafixers.util.Pair;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.flare.util.Obj;
import steve6472.moondust.view.exp.Arithmetic;
import steve6472.moondust.view.exp.Compare;
import steve6472.moondust.view.exp.Const;
import steve6472.moondust.view.property.BooleanProperty;
import steve6472.moondust.view.property.IntegerProperty;
import steve6472.moondust.view.property.Property;
import steve6472.moondust.widget.Panel;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.Properties;
import steve6472.moondust.widget.component.event.OnPropertyChange;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public abstract class PanelView
{
    private static final Logger LOGGER = Log.getLogger(PanelView.class);
    private static final Map<String, Logger> LOGGERS = new HashMap<>();

    // I think I need a way to both bind to a global property from widget definition
    // and to bind to a widget from View (java)

    // not sure if map is needed
    private final Map<String, Property<?>> properties = new HashMap<>();
    private final Map<Key, Consumer<Object>> commandListeners = new HashMap<>();
    protected final Key key;
    protected Panel panel;
    protected final Logger viewLog;

    public PanelView(Key key)
    {
        this.key = key;
        viewLog = LOGGERS.computeIfAbsent("View - " + key, _ -> Log.getLogger("View - " + key));
    }

    public void init(Panel panel)
    {
        if (this.panel != null)
            throw new IllegalStateException("Can not init, Panel already set!");
        this.panel = panel;
        update();
    }

    public void update()
    {
        if (panel == null)
            return;

        properties.clear();
        commandListeners.clear();

        List<Widget> widgets = gatherChildren(panel, new ArrayList<>());
        for (Widget widget : widgets)
        {
            widget.getComponent(Properties.class).ifPresent(properties -> {
                properties.properties().forEach((_, property) -> {
                    property.clear();
                });
            });
        }

        createProperties();
        createCommandListeners();
    }

    public void bind()
    {
        properties.forEach((key, property) ->
        {
            property.addListener((_, oldVal, newVal) ->
            {
                Pair<Widget, Property<?>> byProperty = findByProperty(key);
                if (byProperty == null)
                {
                    Log.warningOnce(viewLog, "Could not find widget for property '%s'".formatted(key));
                    return;
                }

                byProperty.getFirst().handleEvents(OnPropertyChange.class, _ -> true, new OnPropertyChange(key.split("\\.")[0], oldVal, newVal));
            });
        });
    }

    protected abstract void createProperties();
    protected abstract void createCommandListeners();

    protected void addProperty(String key, Property<?> property)
    {
        Property<?> put = properties.put(key, property);
        if (put != null)
            throw new RuntimeException("Can not add property with duplicate key '" + key + "'");
    }

    protected <T extends Property<?>> T findProperty(String property)
    {
        Pair<Widget, Property<?>> byProperty = findByProperty(property);
        if (byProperty == null)
            throw new RuntimeException("Property '%s' was not found".formatted(property));
        //noinspection unchecked
        return (T) byProperty.getSecond();
    }

    protected <T extends Property<?>> T findProperty(String property, Class<T> ignored)
    {
        Pair<Widget, Property<?>> byProperty = findByProperty(property);
        if (byProperty == null)
            throw new RuntimeException("Property '%s' was not found".formatted(property));
        //noinspection unchecked
        return (T) byProperty.getSecond();
    }

    protected void addCommandListener(Key key, Consumer<Object> listener)
    {
        Consumer<Object> put = commandListeners.put(key, listener);
        if (put != null)
            LOGGER.warning("Command Listener for '%s' was replaced".formatted(key));
    }

    private Pair<Widget, Property<?>> findByProperty(String property)
    {
        Obj<Pair<Widget, Property<?>>> ret = Obj.empty();
        List<Widget> children = gatherChildren(panel, new ArrayList<>());
//        Collections.reverse(children);

        // TODO: proper verification
        String[] arr = property.split(":");
        String path = arr[0];
        String propertyName = arr[1];

        for (Widget widget : children)
        {
            widget.getComponent(Properties.class).ifPresent(properties ->
            {
                properties.properties().forEach((k, v) ->
                {
                    if (!k.equals(propertyName))
                        return;
                    if (!widget.getPathWithoutPanel().endsWith(path))
                        return;
                    ret.set(Pair.of(widget, v));
                });
            });

            if (ret.get() != null)
                break;
        }

        return ret.get();
    }

    private List<Widget> gatherChildren(Widget widget, List<Widget> children)
    {
        for (Widget child : widget.getChildren())
        {
            children.add(child);
            gatherChildren(child, children);
        }
        return children;
    }

    /*
     * Outside control
     */

    public void sendCommand(Command command)
    {
        Key key = command.key();
        Consumer<Object> objectConsumer = commandListeners.get(key);
        if (objectConsumer == null)
        {
            LOGGER.warning("Recieved unknown Command '%s' (no command listener exists)".formatted(key));
            return;
        }
        finer("Command '%s' recieved%s".formatted(key, command.value() == null ? "" : " with input: " + command.value()));
        objectConsumer.accept(command.value());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void changeProperty(String propertyName, Object value)
    {
        Property property = properties.get(propertyName);
        if (property == null)
        {
            severe("Property '%s' not found for change".formatted(propertyName));
            return;
        }
        property.set(value);
    }

    @SuppressWarnings({"rawtypes"})
    public Object getPropertyValue(String propertyName)
    {
        Property property = properties.get(propertyName);
        if (property == null)
        {
            severe("Property '%s' not found for get".formatted(propertyName));
            return null;
        }
        return property.get();
    }

    /*
     * Log
     */

    protected void info(String message) { viewLog.info(message); }
    protected void severe(String message) { viewLog.severe(message); }
    protected void fine(String message) { viewLog.fine(message); }
    protected void finer(String message) { viewLog.finer(message); }
    protected void finest(String message) { viewLog.finest(message); }

    /*
     *
     */

    public static void main(String[] args)
    {
        testBoolBind();
        testIntBind();
        testCircularDependencyError();
    }

    private static void testCircularDependencyError()
    {
        IntegerProperty a = new IntegerProperty();
        IntegerProperty b = new IntegerProperty();
        IntegerProperty c = new IntegerProperty();
        a.setDebugName("A");
        b.setDebugName("B");
        c.setDebugName("C");

        a.bind(Arithmetic.Int.add(b, c), b, c);
        b.bind(Arithmetic.Int.add(c, a), c, a);

        DependencyChecker dependencyChecker = new DependencyChecker();

        Exception exception = null;
        try
        {
            dependencyChecker.check(a);
        } catch (Exception e)
        {
            exception = e;
        }

        if (exception == null)
            throw new AssertionError("Exception was expected");
    }

    private static void testIntBind()
    {
        IntegerProperty addCalc = new IntegerProperty();
        IntegerProperty left = new IntegerProperty();
        IntegerProperty right = new IntegerProperty();

//        left.addListener((a, b, c) -> debugChange("left", a, b, c));
//        right.addListener((a, b, c) -> debugChange("right", a, b, c));
//        addCalc.addListener((a, b, c) -> debugChange("addCalc", a, b, c));

        addCalc.bind(Arithmetic.Int.add(left, right), left, right);

        left.set(8);
        if (addCalc.get() != 8)
            throw new AssertionError();
        right.set(12);
        if (addCalc.get() != 20)
            throw new AssertionError();
    }

    private static void testBoolBind()
    {
        IntegerProperty number = new IntegerProperty();
        BooleanProperty show = new BooleanProperty();

//        number.addListener(PanelView::debugChange);
//        show.addListener(PanelView::debugChange);

//        show.bind(number.greaterThan(10));
        show.bind(Compare.Int.greaterThan(number, Const.constInt(10)), number);

        number.set(12);
        if (!show.get())
            throw new AssertionError("Boolean was not updated");
    }

    private static <T> void debugChange(Property<T> property, T oldValue, T newValue)
    {
        System.out.println("Property=" + property.getClass().getSimpleName() + ", old=" + oldValue + ", new=" + newValue);
    }

    private static <T> void debugChange(String name, Property<T> property, T oldValue, T newValue)
    {
        System.out.println("Name=" + name + ", Property=" + property.getClass().getSimpleName() + ", old=" + oldValue + ", new=" + newValue);
    }
}
