package steve6472.moondust.view;

import steve6472.core.registry.Key;
import steve6472.moondust.view.exp.Arithmetic;
import steve6472.moondust.view.exp.Compare;
import steve6472.moondust.view.exp.Const;
import steve6472.moondust.view.property.BooleanProperty;
import steve6472.moondust.view.property.IntegerProperty;
import steve6472.moondust.view.property.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
public abstract class PanelView
{
    // I think I need a way to both bind to a global property from widget definition
    // and to bind to a widget from View (java)

    // not sure if map is needed
    private final Map<String, Property<?>> properties = new HashMap<>();
    private final Map<Key, List<Consumer<Object>>> commandListeners = new HashMap<>();

    public PanelView()
    {
        createProperties();
        createCommandListeners();
    }

    abstract void createProperties();
    abstract void createCommandListeners();

    protected void addProperty(String key, Property<?> property)
    {
        Property<?> put = properties.put(key, property);
        if (put != null)
            throw new RuntimeException("Can not add property with duplicate key '" + key + "'");
    }

    protected void addCommandListener(Key key, Consumer<Object> listener)
    {
        commandListeners.computeIfAbsent(key, _ -> new ArrayList<>()).add(listener);
    }

    /*
     *
     */

    static class ViewTest extends PanelView
    {
        @Override
        void createProperties()
        {

        }

        @Override
        void createCommandListeners()
        {

        }
    }

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
