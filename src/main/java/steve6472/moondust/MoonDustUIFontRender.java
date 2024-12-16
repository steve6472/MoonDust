package steve6472.moondust;

import org.joml.Vector2i;
import steve6472.flare.render.impl.UIFontRenderImpl;
import steve6472.flare.ui.font.render.UITextLine;
import steve6472.moondust.widget.component.MDTextLine;
import steve6472.moondust.widget.component.SpriteOffset;

/**
 * Created by steve6472
 * Date: 12/15/2024
 * Project: MoonDust <br>
 */
public class MoonDustUIFontRender extends UIFontRenderImpl
{
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

            widget.getComponent(MDTextLine.class).ifPresent(mdTextLine ->
            {
                Vector2i position = widget.getPosition();
                widget.getComponent(SpriteOffset.class).ifPresent(offset -> position.add(offset.x, offset.y));
                UITextLine line = mdTextLine.line();
                UITextLine copy = new UITextLine(line.text(), line.size() * pixelScale * 6f, line.style(), line.anchor());
                renderLine(copy, (int) (mdTextLine.offset().x + position.x * pixelScale), (int) ((mdTextLine.offset().y + position.y) * pixelScale));

                //                TextLine textLine = uiTextLine.line();
                //                TextLine textLineCopy = new TextLine(textLine.charEntries(), textLine.size() * pixelScale * 6f, textLine.style(), textLine.anchor(), Billboard.FIXED);

                //                UIFontRender.uiTextRender.line(textLineCopy,
                //                    new Matrix4f().translate((uiTextLine.offset().x + position.x) * pixelScale, (uiTextLine.offset().y + position.y - 2.5f) * pixelScale, 0f));
            });
        });
    }
}
