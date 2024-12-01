package steve6472.moondust;

import steve6472.core.registry.Key;
import steve6472.flare.render.UIRender;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDustUIRender extends UIRender
{
    int pixelScale = 4;

    @Override
    public void render()
    {
        sprite(1, 1, 0, 32, 12, Key.withNamespace("moondust", "box"));
    }

    protected final void sprite(
        int x, int y, float zIndex,
        int width, int height,
        Key textureKey)
    {
        createSprite(x * pixelScale, y * pixelScale, zIndex, width * pixelScale, height * pixelScale, width, height, NO_TINT, getTextureEntry(textureKey));
    }
}
