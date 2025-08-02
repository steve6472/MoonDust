package steve6472.moondust.render;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import steve6472.flare.render.impl.UIFontRenderImpl;
import steve6472.flare.ui.font.render.*;
import steve6472.moondust.MoonDust;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.Bounds;
import steve6472.moondust.widget.component.IBounds;
import steve6472.moondust.widget.component.MDText;

import java.util.Map;

/**
 * Created by steve6472
 * Date: 12/15/2024
 * Project: MoonDust <br>
 */
public class MoonDustUIFontRender extends UIFontRenderImpl
{
    private static final BlueprintFactory EMPTY_BLUEPRINT = new BlueprintFactory(MoonDustConstants.key("dummy_empty_blueprint"), Map.of());

    public MoonDustUIFontRender()
    {
        super(256f);
    }

    @Override
    public void render()
    {
        float pixelScale = MoonDust.getInstance().getPixelScale();

        MoonDust.getInstance().iterate((depth, widget) ->
        {
            if (!widget.isVisible())
                return;

            widget.getComponents(MDText.class).ifPresent(result ->
            {
                MDText mdText = result.comp1();
                Text text = mdText.text();
                if (text.parts().isEmpty())
                    return;

                Text message = text
                    // Adjust to pixel scale
                    .withTextSize(text.textSize() * pixelScale)
                    .withMaxWidth(text.maxWidth() * pixelScale)
                    .withMaxHeight(text.maxHeight() * pixelScale)
                    .withLineGapOffset(text.lineGapOffset() * pixelScale);

                Vector2i pos = new Vector2i();

                // TODO: This has to be reworked
                // Need a little ugly hack so Position is calculated from the immediate parent widget
                // Position was made for widgets, not components lol
                Widget dummy = Widget.withParent(EMPTY_BLUEPRINT, widget);
                dummy.addComponent(new Bounds(new IBounds.Con((int) text.maxWidth()), new IBounds.Con((int) text.maxHeight())));
                mdText.position().evaluatePosition(pos, dummy);

                renderText(message, new Matrix4f().translate(pos.x * pixelScale, pos.y * pixelScale, 1));
            });
        });
    }
}
