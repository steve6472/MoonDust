package steve6472.moondust;

import org.joml.Vector2i;
import steve6472.core.registry.Key;
import steve6472.flare.render.UIRender;
import steve6472.moondust.child.component.SpriteSize;
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
    private static final boolean DEBUG = false;
    int pixelScale = 4;

    Widget testWidget;

    public MoonDustUIRender()
    {
        testWidget = Widget.create(MoonDustRegistries.PANEL_FACTORY.get(Key.withNamespace("moondust", "panel")));
    }

    @Override
    public void render()
    {
        Vector2i position = testWidget.getPosition();
//        sprite(position.x, position.y, 0, 32, 12, Key.withNamespace("moondust", "box"));
        renderWidget(testWidget);
    }

    private void renderWidget(Widget widget)
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
                    sprite(position.x, position.y, 0, spriteSize.width, spriteSize.height, currentSpriteKey);
                });
            });
        });

        widget.getChildren().forEach(this::renderWidget);
    }

    protected final void sprite(
        int x, int y, float zIndex,
        int width, int height,
        Key textureKey)
    {
        if (DEBUG)
            textureKey = Key.withNamespace("moondust", "outline");
        createSprite(x * pixelScale, y * pixelScale, zIndex, width * pixelScale, height * pixelScale, width, height, NO_TINT, getTextureEntry(textureKey));
    }
}
