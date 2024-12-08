package steve6472.moondust;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2i;
import org.joml.Vector3f;
import steve6472.core.registry.Key;
import steve6472.flare.render.UIRender;
import steve6472.flare.ui.textures.SpriteEntry;
import steve6472.moondust.widget.component.Bounds;
import steve6472.moondust.widget.component.SpriteSize;
import steve6472.moondust.widget.Panel;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.CurrentSprite;
import steve6472.moondust.widget.component.Sprites;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDustUIRender extends UIRender
{
    private static boolean DEBUG = false;
    private static boolean DEBUG_BOUNDS = true;

    private static Key DEBUG_OUTLINE = Key.withNamespace("moondust", "outline");
    private static Key DEBUG_OUTLINE_TRANSPARENT = Key.withNamespace("moondust", "outline_transparent");

    int pixelScale = 8;

    Panel testPanel;

    public MoonDustUIRender()
    {
        testPanel = Panel.create(Key.withNamespace("moondust", "panel"));
    }

    float boundsIndex = -256f;
    float widgetIndex = -256f;

    @Override
    public void render()
    {
        boundsIndex = -256f;
        widgetIndex = -256f;
        renderWidget(testPanel);

//        DEBUG = System.currentTimeMillis() % 2000 < 1000;
    }

    private void renderWidget(Widget widget)
    {
        if (widget.isVisible())
        {
            widget.getComponent(CurrentSprite.class).ifPresent(currentSprite -> {
                widget.getComponent(Sprites.class).ifPresent(sprites -> {
                    Key currentSpriteKey = sprites.sprites().get(currentSprite.sprite());
                    if (currentSpriteKey == null)
                    {
                        // TODO: log warning
                        return;
                    }
                    widget.getComponent(SpriteSize.class).ifPresent(spriteSize -> {
                        Vector2i position = widget.getPosition();
                        sprite(position.x, position.y, (widgetIndex += 0.001f), spriteSize.width, spriteSize.height, currentSpriteKey);
                    });
                });
            });
        }

        if (DEBUG_BOUNDS)
        {
            widget.getComponent(Bounds.class).ifPresent(bounds -> {
                Vector2i position = widget.getPosition();
                sprite(position.x, position.y, (boundsIndex += 0.001f), bounds.width, bounds.height, DEBUG_OUTLINE_TRANSPARENT);
            });
        }

        widget.getChildren().forEach(this::renderWidget);
    }

    protected final void sprite(
        int x, int y, float zIndex,
        int width, int height,
        Key textureKey)
    {
        if (DEBUG)
            textureKey = DEBUG_OUTLINE;
        createSprite(x * pixelScale, y * pixelScale, zIndex, width * pixelScale, height * pixelScale, width, height, NO_TINT, getTextureEntry(textureKey));
    }

    // TODO: this (rotation) won't work with parented widgets
    protected void createSprite(int x, int y, float zIndex, int width, int height, int pixelWidth, int pixelHeight, Vector3f tint, SpriteEntry texture, float rotation)
    {
        int index;
        if (texture == null) {
            index = 0;
        } else {
            index = texture.index();
        }

        zIndex /= 256.0F;
        zIndex /= 10.0F;
        Vector3f vtl = new Vector3f((float)x, (float)y, zIndex);
        Vector3f vbl = new Vector3f((float)x, (float)(y + height), zIndex);
        Vector3f vbr = new Vector3f((float)(x + width), (float)(y + height), zIndex);
        Vector3f vtr = new Vector3f((float)(x + width), (float)y, zIndex);

        Quaternionf rot = new Quaternionf();
        rot.rotateZ(rotation);
        Matrix4f mat = new Matrix4f();
        mat.translate((x + width / 2f), (y + height / 2f), 0);
        mat.rotate(rot);
        mat.translate(-(x + width / 2f), -(y + height / 2f), 0);
        vtl.mulPosition(mat);
        vbl.mulPosition(mat);
        vbr.mulPosition(mat);
        vtr.mulPosition(mat);

        Vector3f vertexData = new Vector3f((float)index, (float)pixelWidth, (float)pixelHeight);
        this.vertex(vtl, tint, vertexData);
        this.vertex(vbl, tint, vertexData);
        this.vertex(vbr, tint, vertexData);
        this.vertex(vbr, tint, vertexData);
        this.vertex(vtr, tint, vertexData);
        this.vertex(vtl, tint, vertexData);
    }
}
