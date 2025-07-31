package steve6472.moondust.widget;

import com.mojang.serialization.Codec;
import steve6472.core.registry.StringValue;
import steve6472.moondust.widget.component.event.*;
import steve6472.moondust.widget.component.event.global.OnGlobalMouseButton;
import steve6472.moondust.widget.component.event.global.OnGlobalPixelScaleChange;
import steve6472.moondust.widget.component.event.global.OnGlobalScroll;
import steve6472.moondust.widget.component.event.global.OnGlobalWindowSizeChange;

/**
 * Created by steve6472
 * Date: 5/4/2025
 * Project: MoonDust <br>
 */
public enum UIEventEnum implements StringValue
{
    ON_INPUT("onCharInput", OnCharInput.class),
    ON_DATA_CHANGE("onDataChange", OnDataChange.class),
    ON_DATA_CHANGED("onDataChanged", OnDataChanged.class),
    ON_ENABLE_CHANGE("onEnableChange", OnEnableStateChange.class),
    ON_INIT("onInit", OnInit.class),
    ON_KEY_INPUT("onKeyInput", OnKeyInput.class),
    ON_MOUSE_ENTER("onMouseEnter", OnMouseEnter.class),
    ON_MOUSE_LEAVE("onMouseLeave", OnMouseLeave.class),
    ON_MOUSE_PRESS("onMousePress", OnMousePress.class),
    ON_MOUSE_RELEASE("onMouseRelease", OnMouseRelease.class),
    ON_RENDER("onRender", OnRender.class),
    ON_RANDOM_TICK("onRandomTick", OnRandomTick.class),

    // LUA Only
    // Globals
    ON_GLOBAL_WINDOW_SIZE_CHANGE("onGlobalWindowSizeChange", OnGlobalWindowSizeChange.class),
    ON_GLOBAL_PIXEL_SCALE_CHANGE("onGlobalPixelScaleChange", OnGlobalPixelScaleChange.class),
    ON_GLOBAL_SCROLL("onGlobalScroll", OnGlobalScroll.class),
    ON_GLOBA_MOUSE_BUTTON("onGlobalMouseButton", OnGlobalMouseButton.class)
    ;

    private static final UIEventEnum[] VALS = UIEventEnum.values();
    public static final Codec<UIEventEnum> CODEC = StringValue.fromValues(() -> VALS);

    public final String id;
    public final Class<?> type;

    UIEventEnum(String id, Class<?> type)
    {
        this.id = id;
        this.type = type;
    }

    public String id()
    {
        return id;
    }

    public static UIEventEnum getEnumByType(Class<?> type)
    {
        for (UIEventEnum val : VALS)
        {
            if (val.type.equals(type))
                return val;
        }

        throw new RuntimeException("Could not find Event Enum for " + type.getSimpleName());
    }

    @Override
    public String stringValue()
    {
        return id;
    }
}
