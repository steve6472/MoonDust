package steve6472.moondust.widget;

import org.joml.Vector2i;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.moondust.ComponentRedirect;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.widget.component.*;
import steve6472.moondust.widget.component.event.*;
import steve6472.moondust.widget.component.position.Position;
import steve6472.moondust.core.blueprint.BlueprintFactory;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class Widget implements WidgetComponentGetter
{
    private static final Logger LOGGER = Log.getLogger(Widget.class);

    private final Map<Class<?>, Object> components = new HashMap<>();
    private final Map<String, Widget> children = new HashMap<>();
    private final Widget parent;

    private final InternalStates internalStates;
    private final CustomData customData;

    protected Widget(BlueprintFactory blueprint, Widget parent)
    {
        this.parent = parent;

        // Create InternalStates, a super-default components
        addComponent(internalStates = new InternalStates());

        for (Object component : blueprint.createComponents())
        {
            addComponent(component);
        }

        Object remove = components.remove(WidgetReference.class);
        if (remove instanceof WidgetReference ref)
        {
            BlueprintFactory widgetItself = MoonDustRegistries.WIDGET_FACTORY.get(ref.reference());
            Objects.requireNonNull(widgetItself);
            for (Object component : widgetItself.createComponents())
            {
                if (!components.containsKey(component.getClass()))
                    addComponent(component);
            }
        }

        remove = components.remove(Children.class);
        if (remove instanceof Children cb)
        {
            for (BlueprintFactory child : cb.children())
            {
                Widget childWidget = Widget.withParent(child, this);
                addChild(childWidget);
            }
        }

        remove = components.remove(Overrides.class);
        if (remove instanceof Overrides overrides)
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

    public <T> void addComponent(T component)
    {
        this.components.put(component.getClass(), component);
    }

    public <T extends UIEvent> void handleEvents(Class<T> eventType)
    {
        getEvents(eventType).forEach(e ->
        {
            @SuppressWarnings("unchecked") UIEventCall<T> uiEventCall = (UIEventCall<T>) MoonDustRegistries.EVENT_CALLS.get(e.call());

            if (uiEventCall != null)
            {
                uiEventCall.call(this, eventType.cast(e.event()));
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
        return getComponent(Visible.class).orElseThrow().flag();
    }

    public boolean isEnabled()
    {
        return getComponent(Enabled.class).orElseThrow().flag();
    }

    public boolean isClickable()
    {
        return getComponent(Clickable.class).orElseThrow().flag();
    }

    public boolean isFocusable()
    {
        return getComponent(Focusable.class).orElseThrow().flag();
    }

    public void setVisible(boolean visible)
    {
        addComponent(visible ? Visible.YES : Visible.NO);
    }

    public void setEnabled(boolean enabled)
    {
        addComponent(enabled ? Enabled.YES : Enabled.NO);
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

        String name = getComponent(Name.class).orElse(new Name("-unnamed-")).value();
        return parent.getPath() + "." + name;
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

        return "Widget{" + "parent=" + (parent == null ? "none" : parent.getComponent(Name.class).orElse(new Name("-unnamed-")).value()) + ", components=" + mapped + ", children=" + children + '}';
    }
}
