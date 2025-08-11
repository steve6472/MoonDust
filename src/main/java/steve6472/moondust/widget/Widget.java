package steve6472.moondust.widget;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.flare.Window;
import steve6472.moondust.ComponentRedirect;
import steve6472.moondust.MoonDust;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.Mergeable;
import steve6472.moondust.core.blueprint.Blueprint;
import steve6472.moondust.luau.ProfiledScript;
import steve6472.moondust.luau.global.LuaWidget;
import steve6472.moondust.widget.blueprint.ScriptEntry;
import steve6472.moondust.widget.component.*;
import steve6472.moondust.widget.component.Properties;
import steve6472.moondust.widget.component.event.*;
import steve6472.moondust.widget.component.event.global.OnGlobalMouseButton;
import steve6472.moondust.widget.component.event.global.OnGlobalPixelScaleChange;
import steve6472.moondust.widget.component.event.global.OnGlobalScroll;
import steve6472.moondust.widget.component.event.global.OnGlobalWindowSizeChange;
import steve6472.moondust.widget.component.position.AbsolutePos;
import steve6472.moondust.widget.component.position.Position;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.radiant.LuaTableOps;
import steve6472.radiant.LuauTable;
import steve6472.radiant.LuauUserObject;

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
    public static boolean LOG_LUA_EVENTS = false;
    private static final Logger LOGGER = Log.getLogger(Widget.class);

    private final Key key;
    private final Map<Class<?>, Object> components = new HashMap<>();
    private final Map<String, Widget> children = new LinkedHashMap<>();
    private final Widget parent;

    /// Internal states of the widget, set only from MoonDust, visible for Lua for special conditions (button)
    private final InternalStates internalStates;
    private final WidgetStates states;
    private final CustomData customData;

    protected Widget(BlueprintFactory blueprint, Widget parent)
    {
        this.parent = parent;
        key = blueprint.key();

        // Create InternalStates, a super-default components
        addComponent(internalStates = new InternalStates());
        addComponent(states = new WidgetStates());

        if (this instanceof Panel panel)
        {
            Window window = MoonDust.getInstance().window();
            float pixelScale = MoonDust.getInstance().getPixelScale();
            panel.setBounds((int) (window.getWidth() / pixelScale), (int) (window.getHeight() / pixelScale));
        }

        // Create the actual components
        List<Object> componentsToAdd = new ArrayList<>();
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

                componentsToAdd.addAll(prnt.createComponents());
            }
        });

        // Get the widget reference for parent
        Object refObj = blueprint.blueprints().get(WidgetBlueprints.WIDGET.key());
        if (refObj instanceof Blueprint bp)
            refObj = bp.createComponents().getFirst();

        // If reference from widget definition blueprint is null, check for reference from blueprints
        if (refObj == null)
        {
            Optional<WidgetReference> widgetReference = componentsToAdd
                .stream()
                .filter(o -> o instanceof WidgetReference)
                .findFirst()
                .map(o -> (WidgetReference) o);

            if (widgetReference.isPresent())
            {
                refObj = widgetReference.get();
            }
        }

        // If reference was found, create components
        // Otherwise, if reference is still null, assume empty reference (nothing gets added)
        if (refObj instanceof WidgetReference ref)
        {
            BlueprintFactory widgetItself = MoonDustRegistries.WIDGET_FACTORY.get(ref.reference());
            Objects.requireNonNull(widgetItself, "Could not find " + ref.reference());

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

        // Finally add components from widget definition last for overrides
        for (Object component : componentsToAdd)
        {
            addComponent(component);
        }

        // If custom data was not created via blueprints, add a default empty one
        customData = (CustomData) components.computeIfAbsent(CustomData.class, _ -> new CustomData());
        customData.widget = this;

        if (LOG_WIDGET_CREATION)
            LOGGER.info("Widget: " + getPath());

        getComponent(Children.class).ifPresent(cb -> {
            for (BlueprintFactory child : cb.children())
            {
                Widget childWidget = Widget.withParent(child, this);
                addChild(childWidget);
            }
        });

        addComponent(states.toProperties());

        getComponent(ViewController.class).ifPresent(viewController -> {
            if (!(this instanceof Panel panel))
                throw new RuntimeException("ViewController can only be used on a panel (for now?)");
            viewController.panelView().init(panel);
        });

        postCreateValidation();
        handleEvents(OnInit.class);
    }

    private void postCreateValidation()
    {
        states.validate();

        if (states.focusable.get())
        {
            getComponent(FocusedSprite.class).ifPresentOrElse(_ -> {}, () -> LOGGER.warning(String.format("Widget '%s' is focusable, but lacks '%s' blueprint!", key, WidgetBlueprints.FOCUSED_SPRITE.key())));
        }
    }

//    public static Widget create(Key key)
//    {
//        return create(MoonDustRegistries.WIDGET_FACTORY.get(key));
//    }

//    public static Widget create(BlueprintFactory blueprint)
//    {
//        return new Widget(blueprint, null);
//    }

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
        handleEvents(eventType, test, null);
    }

    private static final Set<Class<?>> IGNORED_JAVA = Set.of(OnGlobalScroll.class);

    public <T extends UIEvent> void handleEvents(Class<T> eventType, Predicate<T> test, @Nullable T override)
    {
        if (!IGNORED_JAVA.contains(eventType))
        {
            getEvents(eventType).forEach(e ->
            {
                @SuppressWarnings("unchecked") UIEventCall<T> uiEventCall = (UIEventCall<T>) MoonDustRegistries.EVENT_CALLS.get(e.call());

                if (uiEventCall != null)
                {
                    T event = override == null ? eventType.cast(e.event()) : override;
                    if (test.test(event))
                    {
                        try
                        {

                            uiEventCall.call(this, event);
                        } catch (ClassCastException ex)
                        {
                            LOGGER.severe("CCE during " + e.call());
                            ex.printStackTrace();
                        }
                    }
                } else
                {
                    Log.warningOnce(LOGGER, "No event call found for '" + e.call() + "' from '" + getPath() + "'");
                }
            });
        }

        getComponent(Scripts.class).ifPresent(scripts -> {
            scripts.scripts().forEach((name, scriptEntry) -> {
                ProfiledScript profiledScript = MoonDustRegistries.LUA_SCRIPT.get(scriptEntry.script());
                if (profiledScript == null)
                {
                    Log.warningOnce(LOGGER, "Could not find script '%s' for '%s' in widget '%s'".formatted(scriptEntry.script(), name, getPath()));
                    return;
                }

                if (!profiledScript.enabled())
                    return;

                profiledScript.setVariable(ProfiledScript.INPUT_VAR, scriptEntry.input() == ScriptEntry.EMPTY ? null : scriptEntry.input());

                UIEventEnum eventEnum = UIEventEnum.getEnumByType(eventType);

                if (LOG_LUA_EVENTS)
                {
                    LOGGER.finest("Running event '" + scriptEntry.script() + "' for '" + eventEnum.id() + "' with " + override);
                }

                try
                {
                    profiledScript.run(eventEnum.id, LuaWidget.createObject(this), eventToUserObject(override));
                } catch (RuntimeException ex)
                {
                    LOGGER.severe("Exception thrown for %s : %s in widget %s".formatted(name, scriptEntry.script(), getPath()));
                    ex.printStackTrace();
                }
            });
        });
    }

    private Object eventToUserObject(Object e)
    {
        if (e == null)
            return new LuauUserObject("null");

        else if (e instanceof OnDataChanged<?> changed)
        {
            // Special handling for tables 'cause I'm too lazy to create proper Codec
            if (changed instanceof OnDataChanged.Table changedTable)
            {
                LuauTable table = new LuauTable();
                table.add("key", changedTable.getChangedKey().toString());
                table.add("type", OnDataChanged.Type.TABLE.stringValue());
                table.add("previous", changedTable.previousValue);
                if (!changedTable.removed)
                    table.add("new", changedTable.newValue);
                return table;
            }

            Codec<?> codec = changed.codec();
            //noinspection unchecked, rawtypes
            DataResult<Object> objectDataResult = ((Codec) codec).encodeStart(LuaTableOps.INSTANCE, e);
            LuauTable table = (LuauTable) objectDataResult.getOrThrow();
            if (changed.removed)
                table.table().remove("new");
            return table;
        }
        else if (e instanceof OnGlobalScroll)
        {
            //noinspection unchecked, rawtypes
            return ((Codec) OnGlobalScroll.CODEC).encodeStart(LuaTableOps.INSTANCE, e).getOrThrow();
        }
        else if (e instanceof OnGlobalMouseButton)
        {
            //noinspection unchecked, rawtypes
            return ((Codec) OnGlobalMouseButton.CODEC).encodeStart(LuaTableOps.INSTANCE, e).getOrThrow();
        } else if (e instanceof OnKeyInput)
        {
            //noinspection unchecked, rawtypes
            return ((Codec) OnKeyInput.CODEC).encodeStart(LuaTableOps.INSTANCE, e).getOrThrow();
        } else if (e instanceof OnCharInput)
        {
            //noinspection unchecked, rawtypes
            return ((Codec) OnCharInput.CODEC).encodeStart(LuaTableOps.INSTANCE, e).getOrThrow();
        } else if (e instanceof OnGlobalWindowSizeChange)
        {
            //noinspection unchecked, rawtypes
            return ((Codec) OnGlobalWindowSizeChange.CODEC).encodeStart(LuaTableOps.INSTANCE, e).getOrThrow();
        } else if (e instanceof OnGlobalPixelScaleChange)
        {
            //noinspection unchecked, rawtypes
            return ((Codec) OnGlobalPixelScaleChange.CODEC).encodeStart(LuaTableOps.INSTANCE, e).getOrThrow();
        } else if (e instanceof OnPropertyChange)
        {
            //noinspection unchecked, rawtypes
            return ((Codec) OnPropertyChange.CODEC).encodeStart(LuaTableOps.INSTANCE, e).getOrThrow();
        }
        else
            throw new RuntimeException("Event to user object not done for " + e);
    }

    /*
     * Required components shorthands
     */

    public void getPosition(Vector2i store)
    {
        getComponent(Position.class).ifPresentOrElse(pos -> pos.evaluatePosition(store, this), () -> {
            Position position = new AbsolutePos(new Vector2i(0, 0));
            addComponent(position);
            position.evaluatePosition(store, this);
        });
    }

    public Vector2i getPosition()
    {
        Vector2i store = new Vector2i();
        getPosition(store);
        return store;
    }

    public Optional<Size> getBounds()
    {
        Optional<Bounds> boundsComponent = getComponent(Bounds.class);
        if (boundsComponent.isEmpty())
            return Optional.empty();
        Bounds bounds = boundsComponent.get();
        Size vec = new Size(0, 0);
        parent().ifPresentOrElse(parent -> {
            Optional<Size> parentBounds = parent.getBounds();
            parentBounds.ifPresentOrElse(parentBoundsVec -> {
                vec.set(bounds.width().calc(parentBoundsVec.width()), bounds.height().calc(parentBoundsVec.height()));
            }, () -> {
                vec.set(bounds.width().calc(0), bounds.height().calc(0));
            });
        }, () -> {
            vec.set(bounds.width().calc(0), bounds.height().calc(0));
        });

        return Optional.of(vec);
    }

    public Optional<Size> getClickboxSize()
    {
        Size vec = new Size(0, 0);
        Optional<ClickboxSize> clickboxSizeComponent = getComponent(ClickboxSize.class);

        clickboxSizeComponent.ifPresentOrElse(clickboxSize -> {
            parent().ifPresentOrElse(parent -> {
                Optional<Size> parentBounds = parent.getBounds();
                parentBounds.ifPresentOrElse(parentBoundsVec -> {
                    vec.set(clickboxSize.width().calc(parentBoundsVec.width()), clickboxSize.height().calc(parentBoundsVec.height()));
                }, () -> {
                    vec.set(clickboxSize.width().calc(0), clickboxSize.height().calc(0));
                });
            }, () -> {
                vec.set(clickboxSize.width().calc(0), clickboxSize.height().calc(0));
            });
        }, () -> {
            getBounds().ifPresent(boundsSize -> {
                vec.set(boundsSize.width(), boundsSize.height());
            });
        });

        return Optional.of(vec);
    }

    public Optional<Size> getSpriteSize()
    {
        Size vec = new Size(0, 0);
        Optional<SpriteSize> spriteSizeComponent = getComponent(SpriteSize.class);

        spriteSizeComponent.ifPresentOrElse(spriteSize -> {
            parent().ifPresentOrElse(parent -> {
                Optional<Size> parentBounds = parent.getBounds();
                parentBounds.ifPresentOrElse(parentBoundsVec -> {
                    vec.set(spriteSize.width().calc(parentBoundsVec.width()), spriteSize.height().calc(parentBoundsVec.height()));
                }, () -> {
                    vec.set(spriteSize.width().calc(0), spriteSize.height().calc(0));
                });
            }, () -> {
                vec.set(spriteSize.width().calc(0), spriteSize.height().calc(0));
            });
        }, () -> {
            getBounds().ifPresent(boundsSize -> {
                vec.set(boundsSize.width(), boundsSize.height());
            });
        });

        return Optional.of(vec);
    }

    public InternalStates internalStates()
    {
        return internalStates;
    }

    public WidgetStates states()
    {
        return states;
    }

    public CustomData customData()
    {
        return customData;
    }

    public boolean isVisible()
    {
        boolean b = states.visible.get();
        if (!b) return false;
        Optional<Widget> parent = parent();
        return parent.map(Widget::isVisible).orElse(true);
    }

    public boolean isEnabled()
    {
        return states.enabled.get();
    }

    public boolean isClickable()
    {
        return states.clickable.get();
    }

    public boolean isFocusable()
    {
        return states.focusable.get();
    }

    public void setVisible(boolean visible)
    {
        states.visible.set(visible);
    }

    public void setEnabled(boolean enabled)
    {
        states.enabled.set(enabled);
        handleEvents(OnEnableStateChange.class, event -> event.enabled().test(enabled));
    }

    public void setClickable(boolean clickable)
    {
        states.clickable.set(clickable);
    }

    public void setFocusable(boolean focusable)
    {
        states.focusable.set(focusable);
    }

    public void setBounds(int width, int height)
    {
        setBounds(new IBounds.Con(width), new IBounds.Con(height));
    }

    public void setBounds(IBounds.Val width, IBounds.Val height)
    {
        getComponent(Bounds.class).ifPresentOrElse(b -> {
            b.width = width;
            b.height = height;
        }, () -> addComponent(new Bounds(width, height)));
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

    public Optional<Widget> panel()
    {
        Optional<Widget> current = parent();

        while (current.isPresent())
        {
            Widget widget = current.get();
            if (widget instanceof Panel)
            {
                return Optional.of(widget);
            }
            current = widget.parent();
        }

        return Optional.empty();
    }

    public String getPath()
    {
        if (parent == null && this instanceof Panel)
            return key.toString();

        if (parent == null)
            return "-root-";

        return parent.getPath() + "." + getName();
    }

    public String getPathWithoutPanel()
    {
        if (parent == null || this instanceof Panel)
            return null;

        String pathWithoutPanel = parent.getPathWithoutPanel();
        if (pathWithoutPanel == null)
            return getName();
        return pathWithoutPanel + "." + getName();
    }

    public Key getKey()
    {
        return key;
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
                T merged = ((Mergeable<T>) mergable).merge((T) comp, component);
                this.components.put(component.getClass(), merged);
                if (component.getClass() == WidgetStates.class)
                    states.setFrom((WidgetStates) merged);
                if (merged instanceof Properties properties)
                    properties.bindToWidget(this);
            }, () ->
            {
                if (component instanceof Properties properties)
                    properties.bindToWidget(this);
                this.components.put(component.getClass(), component);
            });

        } else
        {
            this.components.put(component.getClass(), component);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public <T> boolean removeComponent(Class<T> type)
    {
        if (type == WidgetStates.class)
            throw new RuntimeException("States can not be removed");

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
