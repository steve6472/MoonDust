package steve6472.moondust.widget;

import org.joml.Vector2i;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.moondust.ComponentRedirect;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.Mergeable;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.widget.component.*;
import steve6472.moondust.widget.component.event.*;
import steve6472.moondust.widget.component.flag.Clickable;
import steve6472.moondust.widget.component.flag.Enabled;
import steve6472.moondust.widget.component.flag.Focusable;
import steve6472.moondust.widget.component.flag.Visible;
import steve6472.moondust.widget.component.position.Position;
import steve6472.moondust.core.blueprint.BlueprintFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class Widget implements WidgetComponentGetter
{
    public static boolean LOG_WIDGET_CREATION = false;
    private static final Logger LOGGER = Log.getLogger(Widget.class);

    private final Map<Class<?>, Object> components = new HashMap<>();
    private final Map<String, Widget> children = new LinkedHashMap<>();
    private final Widget parent;

    private final InternalStates internalStates;
    private final CustomData customData;

    protected Widget(BlueprintFactory blueprint, Widget parent)
    {
        this.parent = parent;

        // Create InternalStates, a super-default components
        addComponent(internalStates = new InternalStates());

        Object temp = blueprint.blueprints().get(WidgetBlueprints.WIDGET.key());
        if (temp instanceof Blueprint bp)
            temp = bp.createComponents().getFirst();
        if (temp instanceof WidgetReference ref)
        {
            BlueprintFactory widgetItself = MoonDustRegistries.WIDGET_FACTORY.get(ref.reference());
            Objects.requireNonNull(widgetItself);

            widgetItself.blueprints().forEach((key, bp) ->
            {
                if (LOG_WIDGET_CREATION)
                    LOGGER.fine("Creating from Widget: " + key);
                for (Object component : bp.createComponents())
                {
                    addComponent(component);
                }
            });
        }

        // Create the actual components
        blueprint.blueprints().forEach((key, prnt) ->
        {
            if (WidgetBlueprints.WIDGET.key().equals(key))
            {
                if (LOG_WIDGET_CREATION)
                    LOGGER.finer("Creating from Blueprint: " + key + " (ignored!)");
            } else
            {
                if (LOG_WIDGET_CREATION)
                    LOGGER.finer("Creating from Blueprint: " + key);
                for (Object component : prnt.createComponents())
                {
                    addComponent(component);
                }
            }
        });

        temp = components.remove(Overrides.class);
        if (temp instanceof Overrides overrides)
        {
            //noinspection rawtypes
            for (BlueprintOverride override : overrides.overrides())
            {
                //noinspection unchecked
                Optional<?> component = getComponent(override.target());
                if (component.isEmpty())
                {
                    throw new RuntimeException("Override present, but component for type %s not found".formatted(override.target().getSimpleName()));
                }
                //noinspection unchecked
                Object overriden = override.override(component.get());
                components.remove(override.target());
                addComponent(overriden);
            }
        }

        // If custom data was not created via blueprints, add a default empty one
        customData = (CustomData) components.computeIfAbsent(CustomData.class, _ -> new CustomData());

        if (LOG_WIDGET_CREATION)
            LOGGER.info("Widget: " + getPath());

        getComponent(Children.class).ifPresent(cb -> {
            for (BlueprintFactory child : cb.children())
            {
                Widget childWidget = Widget.withParent(child, this);
                addChild(childWidget);
            }
        });

        handleEvents(OnInit.class);
    }

    public static Widget create(Key key)
    {
        return create(MoonDustRegistries.WIDGET_FACTORY.get(key));
    }

    public static Widget create(BlueprintFactory blueprint)
    {
        return new Widget(blueprint, null);
    }

    public static Widget withParent(BlueprintFactory blueprint, Widget parent)
    {
        return new Widget(blueprint, parent);
    }

    /*
     * Control
     */

    public void addChild(Widget widget)
    {
        Name name = widget.getComponent(Name.class).orElseThrow();
        if (children.containsKey(name.value()))
            throw new IllegalStateException("Child widget with name '" + name.value() + "' already exists!");
        children.put(name.value(), widget);
    }

    public boolean removeChild(String name)
    {
        return children.remove(name) != null;
    }

    public <T extends UIEvent> void handleEvents(Class<T> eventType)
    {
        handleEvents(eventType, _ -> true);
    }

    public <T extends UIEvent> void handleEvents(Class<T> eventType, Predicate<T> test)
    {
        getEvents(eventType).forEach(e ->
        {
            @SuppressWarnings("unchecked") UIEventCall<T> uiEventCall = (UIEventCall<T>) MoonDustRegistries.EVENT_CALLS.get(e.call());

            if (uiEventCall != null)
            {
                T event = eventType.cast(e.event());
                if (test.test(event))
                {
                    uiEventCall.call(this, event);
                }
            } else
            {
                LOGGER.warning("No event call found for " + e.call());
            }
        });
    }

    /*
     * Required components shorthands
     */

    public void getPosition(Vector2i store)
    {
        getComponent(Position.class).orElseThrow().evaluatePosition(store, this);
    }

    public Vector2i getPosition()
    {
        Vector2i store = new Vector2i();
        getPosition(store);
        return store;
    }

    public InternalStates internalStates()
    {
        return internalStates;
    }

    public CustomData customData()
    {
        return customData;
    }

    public boolean isVisible()
    {
        return getComponent(Visible.class).orElseThrow(() -> new RuntimeException("Visible component not found!")).flag();
    }

    public boolean isEnabled()
    {
        return getComponent(Enabled.class).orElseThrow(() -> new RuntimeException("Enabled component not found!")).flag();
    }

    public boolean isClickable()
    {
        return getComponent(Clickable.class).orElseThrow(() -> new RuntimeException("Clickable component not found!")).flag();
    }

    public boolean isFocusable()
    {
        return getComponent(Focusable.class).orElseThrow(() -> new RuntimeException("Focusable component not found!")).flag();
    }

    public void setVisible(boolean visible)
    {
        addComponent(visible ? Visible.YES : Visible.NO);
    }

    public void setEnabled(boolean enabled)
    {
        addComponent(enabled ? Enabled.YES : Enabled.NO);
        handleEvents(OnEnableStateChange.class, event -> event.enabled().test(enabled));
    }

    public void setClickable(boolean clickable)
    {
        addComponent(clickable ? Clickable.YES : Clickable.NO);
    }

    public void setFocusable(boolean focusable)
    {
        addComponent(focusable ? Focusable.YES : Focusable.NO);
    }

    public void setBounds(int width, int height)
    {
        Bounds bounds = getComponent(Bounds.class).orElseThrow();
        bounds.width = width;
        bounds.height = height;
    }

    public String getName()
    {
        return getComponent(Name.class).orElse(new Name("-unnamed-")).value();
    }

    /*
     * Getters
     */

    public <T extends UIEvent> List<UIEventCallEntry> getEvents(Class<T> eventType)
    {
        Optional<UIEvents> component = getComponent(UIEvents.class);
        if (component.isEmpty())
            return List.of();
        UIEvents uiEvents = component.get();
        return uiEvents.events().stream().filter(event -> event.event().getClass().equals(eventType)).filter(event -> event.condition().test(this)).toList();
    }

    public Optional<Widget> parent()
    {
        return Optional.ofNullable(parent);
    }

    public String getPath()
    {
        if (parent == null)
            return "-root-";

        return parent.getPath() + "." + getName();
    }

    public Optional<Widget> getChild(String name)
    {
        return Optional.ofNullable(children.get(name));
    }

    public Collection<Widget> getChildren()
    {
        return List.copyOf(children.values());
    }

    @Override
    public <T> Optional<T> getComponent(Class<T> type)
    {
        Collection<Class<?>> redirectClasses = ComponentRedirect.get(type);
        if (redirectClasses != null)
        {
            for (Class<?> redirectClass : redirectClasses)
            {
                Object o = components.get(redirectClass);
                if (o != null)
                    //noinspection unchecked
                    return Optional.of((T) o);
            }
            return Optional.empty();
        }

        //noinspection unchecked
        return Optional.ofNullable((T) components.get(type));
    }

    /// Does NOT merge components
    public <T> void replaceComponent(T component)
    {
        removeComponent(component.getClass());
        addComponent(component);
    }

    /// Can merge components
    public <T> void addComponent(T component)
    {
        if (component instanceof Mergeable<?> mergable)
        {
            Optional<?> existing = getComponent(component.getClass());

            existing.ifPresentOrElse(comp ->
            {
                //noinspection unchecked
                this.components.put(component.getClass(), ((Mergeable<T>) mergable).merge((T) comp, component));
            }, () -> this.components.put(component.getClass(), component));

        } else
        {
            this.components.put(component.getClass(), component);
        }
    }

    public <T> boolean removeComponent(Class<T> type)
    {
        Collection<Class<?>> redirectClasses = ComponentRedirect.get(type);
        if (redirectClasses != null)
        {
            for (Class<?> redirectClass : redirectClasses)
            {
                Object o = components.remove(redirectClass);
                if (o != null)
                    return true;
            }
            return false;
        }

        return components.remove(type) != null;
    }

    protected boolean iterateChildren(Function<Widget, Boolean> stopCondition)
    {
        if (stopCondition.apply(this))
            return true;

        for (Widget child : getChildren())
        {
            if (child.iterateChildren(stopCondition))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString()
    {
        Map<String, Object> mapped = new HashMap<>();
        components.forEach((key, value) -> mapped.put(key.getSimpleName(), value));

        return "Widget{" + "parent=" + (parent == null ? "none" : parent.getName()) + ", components=" + mapped + ", children=" + children + '}';
    }
}
