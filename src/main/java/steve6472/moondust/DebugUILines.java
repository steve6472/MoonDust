package steve6472.moondust;

import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.flare.render.debug.DebugRender;
import steve6472.flare.render.impl.UILineRenderImpl;
import steve6472.moondust.widget.component.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 12/19/2024
 * Project: MoonDust <br>
 */
public class DebugUILines extends UILineRenderImpl
{
    private static final Vector4f BOUNDS = DebugRender.WHITE;
    private static final Vector4f SPRITE_SIZE = DebugRender.DARK_RED;
    private static final Vector4f CLICKBOX = DebugRender.DARK_GREEN;
    private static final Vector4f CHARACTER = DebugRender.GOLD;
    private static final Vector4f TEXT = DebugRender.BROWN;
    private static final Vector4f MESSAGE = DebugRender.CORAL;

    public static boolean bounds = false;
    public static boolean spriteSize = false;
    public static boolean clickbox = false;
    public static boolean character = false;
    public static boolean textLine = false;
    public static boolean message = false;

    public static final List<Vector2i> CHARACTERS = new ArrayList<>(1024);
    public static final List<Vector2i> TEXT_LINES = new ArrayList<>(512);
    public static final List<Vector2i> MESSAGES = new ArrayList<>(256);

    @Override
    public void render()
    {
        MoonDust.getInstance().iterate(widget ->
        {
            if (spriteSize)
            {
                widget.getComponents(SpriteSize.class, CurrentSprite.class).ifPresent(comp ->
                {
                    SpriteSize spriteSize = comp.comp1();
                    Vector2i position = widget.getPosition();
                    widget.getComponent(SpriteOffset.class).ifPresent(offset -> position.add(offset.x, offset.y));

                    rectangleScaled(position.x, position.y, spriteSize.width, spriteSize.height, SPRITE_SIZE);
                });
            }

            if (clickbox)
            {
                widget.getComponent(ClickboxSize.class).ifPresent(clickboxSize ->
                {
                    Vector2i clickboxPosition = widget.getPosition();
                    widget.getComponent(ClickboxOffset.class).ifPresent(offset -> clickboxPosition.add(offset.x, offset.y));

                    rectangleScaled(clickboxPosition.x, clickboxPosition.y, clickboxSize.width, clickboxSize.height, CLICKBOX);
                });
            }

            if (bounds)
            {
                widget.getComponent(Bounds.class).ifPresent(bounds ->
                {
                    Vector2i position = widget.getPosition();
                    rectangleScaled(position.x, position.y, bounds.width, bounds.height, BOUNDS);
                });
            }
        });

        if (character)
        {
            for (int i = 0; i < CHARACTERS.size() / 2; i++)
            {
                Vector2i start = CHARACTERS.get(i * 2);
                Vector2i end = CHARACTERS.get(i * 2 + 1);
                // start and end are already scaled
                line(start, end, CHARACTER);
            }
            CHARACTERS.clear();
        }

        if (textLine)
        {
            for (int i = 0; i < TEXT_LINES.size() / 2; i++)
            {
                Vector2i start = TEXT_LINES.get(i * 2);
                Vector2i end = TEXT_LINES.get(i * 2 + 1);
                // start and end are already scaled
                line(start, end, TEXT);
            }
            TEXT_LINES.clear();
        }

        if (message)
        {
            for (int i = 0; i < MESSAGES.size() / 2; i++)
            {
                Vector2i start = MESSAGES.get(i * 2);
                Vector2i end = MESSAGES.get(i * 2 + 1);
                // start and end are already scaled
                line(start, end, MESSAGE);
            }
            MESSAGES.clear();
        }
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
