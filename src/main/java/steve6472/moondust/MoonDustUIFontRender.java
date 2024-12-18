package steve6472.moondust;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import steve6472.core.registry.Key;
import steve6472.flare.render.impl.UIFontRenderImpl;
import steve6472.flare.ui.font.Font;
import steve6472.flare.ui.font.layout.GlyphInfo;
import steve6472.flare.ui.font.render.*;
import steve6472.flare.ui.font.style.FontStyleEntry;
import steve6472.moondust.widget.component.Bounds;
import steve6472.moondust.widget.component.MDTextLine;
import steve6472.moondust.widget.component.SpriteOffset;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/15/2024
 * Project: MoonDust <br>
 */
public class MoonDustUIFontRender extends UIFontRenderImpl
{
    private static final Key DEFAULT_FONT = Key.withNamespace(MoonDustConstants.NAMESPACE, "tiny_pixie2");

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

            widget.getComponents(MDTextLine.class).ifPresent(result ->
            {
                MDTextLine mdTextLine = result.comp1();
                if (mdTextLine.line().text().isBlank())
                    return;

                Vector2i position = widget.getPosition();
                widget.getComponent(SpriteOffset.class).ifPresent(offset -> position.add(offset.x, offset.y));
                UITextLine line = mdTextLine.line();
                UITextLine copy = new UITextLine(line.text(), UITextLine.MESSAGE_SIZE, line.style(), line.anchor());

                float maxWidth = widget.getComponent(Bounds.class).orElseGet(() -> new Bounds(Integer.MAX_VALUE, 0)).width;

                renderMessage(new UITextMessage(List.of(copy), line.size() * pixelScale * 6f, maxWidth * pixelScale, line.anchor(), Align.CENTER, NewLineType.MAX_HEIGHT, 0f),
                    new Matrix4f().translate((mdTextLine.offset().x + position.x) * pixelScale, (mdTextLine.offset().y + position.y) * pixelScale, 0));

                //                TextLine textLine = uiTextLine.line();
                //                TextLine textLineCopy = new TextLine(textLine.charEntries(), textLine.size() * pixelScale * 6f, textLine.style(), textLine.anchor(), Billboard.FIXED);

                //                UIFontRender.uiTextRender.line(textLineCopy,
                //                    new Matrix4f().translate((uiTextLine.offset().x + position.x) * pixelScale, (uiTextLine.offset().y + position.y - 2.5f) * pixelScale, 0f));
            });
        });
    }

    protected void renderLinePixel(UITextLine textLine, float x, float y)
    {
        String text = textLine.text();
        if (text.isBlank())
            return;

        FontStyleEntry style = textLine.style();
        float size = textLine.size();
        Anchor2D anchor = textLine.anchor();

        float pixelScale = MoonDust.getInstance().getPixelScale();
        Font font = style.style().font();
        float maxHeight = font.getMaxHeight(text, size);
        float textWidth = font.getWidth(text, size);

        Vector2f alignOffset = new Vector2f();
        anchor.applyOffset(alignOffset, textWidth, 0, maxHeight);

        float cumAdvance = 0;
        for (char c : text.toCharArray())
        {
            GlyphInfo glyphInfo = font.glyphInfo(c);
            float offsetY = maxHeight - glyphInfo.planeBounds().height() * size;
            Vector2f off = new Vector2f(cumAdvance, offsetY);
            off.add(alignOffset);
            off.add(x * pixelScale, y * pixelScale);
            if (style.key().equals(DEFAULT_FONT))
                off.sub(pixelScale / 3f, pixelScale / 3f);
            renderChar(font, glyphInfo, off, size, style.index(), new Matrix4f());
            cumAdvance += glyphInfo.advance() * size;
        }
    }
}
