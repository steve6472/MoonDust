package steve6472.moondust.widget.component;

import steve6472.moondust.core.Mergeable;
import steve6472.moondust.view.property.BooleanProperty;

import java.util.Map;

/**
 * Created by steve6472
 * Date: 7/13/2025
 * Project: MoonDust <br>
 */
public class WidgetStates implements Mergeable<WidgetStates>
{
    public static final boolean DEFAULT_ENABLED = true;
    public static final boolean DEFAULT_VISIBLE = true;
    public static final boolean DEFAULT_CLICKABLE = false;
    public static final boolean DEFAULT_FOCUSABLE = false;

//    public static final Codec<WidgetStates> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//        Codec.BOOL.optionalFieldOf("enabled", DEFAULT_ENABLED).forGetter(o -> o.enabled.get()),
//        Codec.BOOL.optionalFieldOf("visible", DEFAULT_VISIBLE).forGetter(o -> o.visible.get()),
//        Codec.BOOL.optionalFieldOf("clickable", DEFAULT_CLICKABLE).forGetter(o -> o.clickable.get()),
//        Codec.BOOL.optionalFieldOf("focusable", DEFAULT_FOCUSABLE).forGetter(o -> o.focusable.get())
//    ).apply(instance, WidgetStates::new));

    public BooleanProperty enabled;
    public BooleanProperty visible;
    public BooleanProperty clickable;
    public BooleanProperty focusable;

    public WidgetStates(Boolean enabled, Boolean visible, Boolean clickable, Boolean focusable)
    {
        this.enabled = enabled == null ? null : new BooleanProperty(enabled);
        this.visible = visible == null ? null : new BooleanProperty(visible);
        this.clickable = clickable == null ? null : new BooleanProperty(clickable);
        this.focusable = focusable == null ? null : new BooleanProperty(focusable);

        if (this.enabled != null) this.enabled.setDebugName("enabled");
        if (this.visible != null) this.visible.setDebugName("visible");
        if (this.clickable != null) this.clickable.setDebugName("clickable");
        if (this.focusable != null) this.focusable.setDebugName("focusable");
    }

    public WidgetStates()
    {
        this(DEFAULT_ENABLED, DEFAULT_VISIBLE, DEFAULT_CLICKABLE, DEFAULT_FOCUSABLE);
    }

    public static WidgetStates enabled(boolean enabled)
    {
        return new WidgetStates(enabled, null, null, null);
    }

    public static WidgetStates visible(boolean visible)
    {
        return new WidgetStates(null, visible, null, null);
    }

    public static WidgetStates clickable(boolean clickable)
    {
        return new WidgetStates(null, null, clickable, null);
    }

    public static WidgetStates focusable(boolean focusable)
    {
        return new WidgetStates(null, null, null, focusable);
    }

    private Boolean select(BooleanProperty left, BooleanProperty right)
    {
        if (right == null) return left.get();
        return right.get();
    }

    @Override
    public WidgetStates merge(WidgetStates left, WidgetStates right)
    {
        Boolean enabled = select(left.enabled, right.enabled);
        Boolean visible = select(left.visible, right.visible);
        Boolean clickable = select(left.clickable, right.clickable);
        Boolean focusable = select(left.focusable, right.focusable);

        return new WidgetStates(enabled, visible, clickable, focusable);
    }

    public void validate()
    {
        if (enabled == null) throw new IllegalStateException("Widget States have an unspecified value: enabled");
        if (visible == null) throw new IllegalStateException("Widget States have an unspecified value: visible");
        if (clickable == null) throw new IllegalStateException("Widget States have an unspecified value: clickable");
        if (focusable == null) throw new IllegalStateException("Widget States have an unspecified value: focusable");
    }

    public Properties toProperties()
    {
        return new Properties(Map.of(
            "enabled", enabled,
            "visible", visible,
            "clickable", clickable,
            "focusable", focusable)
        );
    }

    public void setFrom(WidgetStates states)
    {
        enabled = states.enabled;
        visible = states.visible;
        clickable = states.clickable;
        focusable = states.focusable;
    }

    @Override
    public String toString()
    {
        return "WidgetStates{" + "enabled=" + enabled + ", visible=" + visible + ", clickable=" + clickable + ", focusable=" + focusable + '}';
    }
}
