package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import steve6472.moondust.core.Mergeable;

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

    public static final Codec<WidgetStates> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("enabled", DEFAULT_ENABLED).forGetter(o -> o.enabled),
        Codec.BOOL.optionalFieldOf("visible", DEFAULT_VISIBLE).forGetter(o -> o.visible),
        Codec.BOOL.optionalFieldOf("clickable", DEFAULT_CLICKABLE).forGetter(o -> o.clickable),
        Codec.BOOL.optionalFieldOf("focusable", DEFAULT_FOCUSABLE).forGetter(o -> o.focusable)
    ).apply(instance, WidgetStates::new));

    public Boolean enabled;
    public Boolean visible;
    public Boolean clickable;
    public Boolean focusable;

    public WidgetStates(Boolean enabled, Boolean visible, Boolean clickable, Boolean focusable)
    {
        this.enabled = enabled;
        this.visible = visible;
        this.clickable = clickable;
        this.focusable = focusable;
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

    private Boolean select(Boolean left, Boolean right)
    {
        if (right == null) return left;
        return right;
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
