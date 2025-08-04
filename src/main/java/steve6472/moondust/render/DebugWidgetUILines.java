package steve6472.moondust.render;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.flare.render.debug.DebugRender;
import steve6472.flare.render.impl.UILineRenderImpl;
import steve6472.moondust.MoonDust;
import steve6472.moondust.widget.component.*;

/**
 * Created by steve6472
 * Date: 12/19/2024
 * Project: MoonDust <br>
 */
public class DebugWidgetUILines extends UILineRenderImpl
{
    private static final Vector4f BOUNDS = DebugRender.WHITE;
    private static final Vector4f SPRITE_SIZE = DebugRender.DARK_RED;
    private static final Vector4f CLICKBOX = DebugRender.DARK_GREEN;

    public static boolean bounds = false;
    public static boolean spriteSize = false;
    public static boolean clickbox = false;

    @Override
    public void render()
    {
        MoonDust.getInstance().iterate(widget ->
        {
            if (spriteSize)
            {
                widget.getSpriteSize().ifPresent(spriteSize -> {
                    if (widget.getComponent(CurrentSprite.class).isEmpty())
                        return;
                    if (!widget.isVisible())
                        return;

                    Vector2i position = widget.getPosition();
                    widget.getComponent(SpriteOffset.class).ifPresent(offset -> position.add(offset.x, offset.y));

                    rectangleScaled(position.x, position.y, spriteSize.width(), spriteSize.height(), SPRITE_SIZE);
                });
            }

            if (clickbox)
            {
                widget.getClickboxSize().ifPresent(clickboxSize ->
                {
                    if (!widget.isClickable())
                        return;
                    if (!widget.isVisible())
                        return;

                    Vector2i clickboxPosition = widget.getPosition();
                    widget.getComponent(ClickboxOffset.class).ifPresent(offset -> clickboxPosition.add(offset.x, offset.y));

                    rectangleScaled(clickboxPosition.x, clickboxPosition.y, clickboxSize.width(), clickboxSize.height(), CLICKBOX);
                });
            }

            if (bounds)
            {
                widget.getBounds().ifPresent(bounds ->
                {
                    if (!widget.isVisible())
                        return;

                    Vector2i position = widget.getPosition();
                    rectangleScaled(position.x, position.y, bounds.width(), bounds.height(), BOUNDS);
                });
            }
        });
    }

    private void rectangleScaled(float x, float y, float width, float height, Vector4f color)
    {
        float pixelScale = MoonDust.getInstance().getPixelScale();
        rectangle(new Vector3f(x * pixelScale + 1, y * pixelScale + 1, 0), new Vector3f((x + width) * pixelScale, (y + height) * pixelScale, 0), color);
    }

    private void lineScaled(Vector2i start, Vector2i end, Vector4f color)
    {
        float pixelScale = MoonDust.getInstance().getPixelScale();
        line(new Vector3f(start.x * pixelScale, start.y * pixelScale, 0), new Vector3f(end.x * pixelScale, end.y * pixelScale, 0), color);
    }

    private void line(Vector2i start, Vector2i end, Vector4f color)
    {
        line(new Vector3f(start.x, start.y, 0), new Vector3f(end.x, end.y, 0), color);
    }
}
