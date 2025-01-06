package steve6472.moondust;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import steve6472.core.registry.Key;
import steve6472.flare.render.impl.UIFontRenderImpl;
import steve6472.flare.ui.font.Font;
import steve6472.flare.ui.font.UnknownCharacter;
import steve6472.flare.ui.font.layout.GlyphInfo;
import steve6472.flare.ui.font.render.*;
import steve6472.flare.util.FloatUtil;
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

    @Override
    protected void renderMessage(UITextMessage message, Matrix4f transform)
    {
        List<UIMessageSegment> messageSegments = message.createSegments();
        final float maxWidth = messageSegments.stream().map(s -> s.width).max(Float::compare).orElse(0f);
        final float totalHeight = messageSegments.stream().map(s -> s.height).collect(FloatUtil.summing(Float::floatValue));

        Vector2f offset = new Vector2f();
        // charIndex, lineNumber
        int[] indicies = {0, 0};

        if (message.align() == Align.RIGHT)
        {
            message.anchor().applyOffset(offset, maxWidth, 0, totalHeight);
            offset.x += maxWidth - messageSegments.getFirst().width;
        } else if (message.align() == Align.CENTER)
        {
            message.anchor().applyOffset(offset, maxWidth, 0, totalHeight);
            offset.x += maxWidth / 2f - messageSegments.getFirst().width / 2f;
        }

        if (DebugUILines.textLine)
        {
            Vector3f vtl = new Vector3f(0, 0, 0).add(offset.x, offset.y, 0).mulPosition(transform);
            Vector3f vtr = new Vector3f(messageSegments.getFirst().width, 0, 0).add(offset.x, offset.y, 0).mulPosition(transform);
            Vector3f vbl = new Vector3f(0, messageSegments.getFirst().height, 0).add(offset.x, offset.y, 0).mulPosition(transform);
            Vector3f vbr = new Vector3f(messageSegments.getFirst().width, messageSegments.getFirst().height, 0).add(offset.x, offset.y, 0).mulPosition(transform);
            // TOP
            DebugUILines.TEXT_LINES.add(new Vector2i((int) vtl.x, (int) vtl.y));
            DebugUILines.TEXT_LINES.add(new Vector2i((int) vtr.x, (int) vtr.y));

            // BOTTOM
            DebugUILines.TEXT_LINES.add(new Vector2i((int) vbl.x, (int) vbl.y));
            DebugUILines.TEXT_LINES.add(new Vector2i((int) vbr.x, (int) vbr.y));

            // LEFT
            DebugUILines.TEXT_LINES.add(new Vector2i((int) vtl.x, (int) vtl.y));
            DebugUILines.TEXT_LINES.add(new Vector2i((int) vtl.x, (int) vbr.y));

            // RIGHT
            DebugUILines.TEXT_LINES.add(new Vector2i((int) vtr.x, (int) vtr.y));
            DebugUILines.TEXT_LINES.add(new Vector2i((int) vtr.x, (int) vbr.y));
        }

        if (DebugUILines.message)
        {
            Vector3f vtl = new Vector3f(0, 0, 0).add(offset.x, offset.y, 0).mulPosition(transform);
            Vector3f vtr = new Vector3f(maxWidth, 0, 0).add(offset.x, offset.y, 0).mulPosition(transform);
            Vector3f vbl = new Vector3f(0, totalHeight, 0).add(offset.x, offset.y, 0).mulPosition(transform);
            Vector3f vbr = new Vector3f(maxWidth, totalHeight, 0).add(offset.x, offset.y, 0).mulPosition(transform);
            // TOP
            DebugUILines.MESSAGES.add(new Vector2i((int) vtl.x, (int) vtl.y));
            DebugUILines.MESSAGES.add(new Vector2i((int) vtr.x, (int) vtr.y));

            // BOTTOM
            DebugUILines.MESSAGES.add(new Vector2i((int) vbl.x, (int) vbl.y));
            DebugUILines.MESSAGES.add(new Vector2i((int) vbr.x, (int) vbr.y));

            // LEFT
            DebugUILines.MESSAGES.add(new Vector2i((int) vtl.x, (int) vtl.y));
            DebugUILines.MESSAGES.add(new Vector2i((int) vtl.x, (int) vbr.y));

            // RIGHT
            DebugUILines.MESSAGES.add(new Vector2i((int) vtr.x, (int) vtr.y));
            DebugUILines.MESSAGES.add(new Vector2i((int) vtr.x, (int) vbr.y));
        }

        message.iterateCharacters((character, nextCharacter) ->
        {
            GlyphInfo glyph = character.glyph();
            float size = character.size();

            if (glyph == null)
                throw new RuntimeException("Glyph for some character not found not found!");

            Font font = character.style().style().font();

            float kerningAdvance = 0f;
            if (nextCharacter != null && font == nextCharacter.style().style().font())
            {
                kerningAdvance = font.kerningAdvance((char) glyph.index(), (char) nextCharacter.glyph().index());
            }

            // Todo: somehow handle breakIndicies of 0 at index 0
            boolean newLine = false;
            for (UIMessageSegment messageSegment : messageSegments)
            {
                if (messageSegment.end == indicies[0])
                {
                    newLine = true;
                    break;
                }
            }
            if (newLine)
            {
                offset.x = 0;
                indicies[1]++;
                if (message.align() == Align.RIGHT)
                {
                    Vector2f offTemp = new Vector2f();
                    message.anchor().applyOffset(offTemp, maxWidth, 0, totalHeight);
                    offset.x = offTemp.x;
                    offset.x += maxWidth - messageSegments.get(indicies[1]).width;
                } else if (message.align() == Align.CENTER)
                {
                    Vector2f offTemp = new Vector2f();
                    message.anchor().applyOffset(offTemp, maxWidth, 0, totalHeight);
                    offset.x = offTemp.x;
                    offset.x += maxWidth / 2f - messageSegments.get(indicies[1]).width / 2f;
                }

                if (message.newLineType() == NewLineType.MAX_HEIGHT)
                    offset.y += messageSegments.get(indicies[1] - 1).height + messageSegments.get(indicies[1] - 1).maxDescent - messageSegments.get(indicies[1]).minDescent;
                else if (message.newLineType() == NewLineType.FIXED)
                    offset.y += font.getMetrics().lineHeight() * size + message.lineGapOffset() * size;
            }

            if (!glyph.isInvisible())
            {
                UIMessageSegment segment = messageSegments.get(indicies[1]);
                float offsetY = segment.height - glyph.planeBounds().height() * size;
                offsetY -= segment.minDescent - glyph.planeBounds().bottom() * size;
                renderChar(font, glyph, new Vector2f(offset).add(0, offsetY), size, character.style().index(), transform);
            }

            offset.x += glyph.advance() * size;
            offset.x += kerningAdvance * size;

            indicies[0]++;
        });
    }

    @Override
    protected void renderChar(Font font, GlyphInfo glyphInfo, Vector2f offset, float size, int styleIndex, Matrix4f transform) {

        if (glyphInfo == UnknownCharacter.unknownGlyph()) {
            font = UnknownCharacter.fontEntry().font();
            styleIndex = UnknownCharacter.styleEntry().index();
        }

        Vector2f tl = (new Vector2f(glyphInfo.atlasBounds().left(), glyphInfo.atlasBounds().top())).div((float)font.getAtlasData().width(), (float)font.getAtlasData().height());
        Vector2f br = (new Vector2f(glyphInfo.atlasBounds().right(), glyphInfo.atlasBounds().bottom())).div((float)font.getAtlasData().width(), (float)font.getAtlasData().height());
        float xpos = offset.x;
        float ypos = offset.y;
        float w = glyphInfo.planeBounds().width() * size;
        float h = glyphInfo.planeBounds().height() * size;
        Vector3f vtl = (new Vector3f(xpos, ypos, 0.0F)).mulPosition(transform);
        Vector3f vbl = (new Vector3f(xpos, ypos + h, 0.0F)).mulPosition(transform);
        Vector3f vbr = (new Vector3f(xpos + w, ypos + h, 0.0F)).mulPosition(transform);
        Vector3f vtr = (new Vector3f(xpos + w, ypos, 0.0F)).mulPosition(transform);
        this.vertex(vtl, new Vector2f(tl.x, tl.y), styleIndex);
        this.vertex(vbl, new Vector2f(tl.x, br.y), styleIndex);
        this.vertex(vbr, new Vector2f(br.x, br.y), styleIndex);
        this.vertex(vbr, new Vector2f(br.x, br.y), styleIndex);
        this.vertex(vtr, new Vector2f(br.x, tl.y), styleIndex);
        this.vertex(vtl, new Vector2f(tl.x, tl.y), styleIndex);

        if (DebugUILines.character)
        {
            // TOP
            DebugUILines.CHARACTERS.add(new Vector2i((int) vtl.x, (int) vtl.y));
            DebugUILines.CHARACTERS.add(new Vector2i((int) vtr.x, (int) vtr.y));

            // BOTTOM
            DebugUILines.CHARACTERS.add(new Vector2i((int) vbl.x, (int) vbl.y));
            DebugUILines.CHARACTERS.add(new Vector2i((int) vbr.x, (int) vbr.y));

            // LEFT
            DebugUILines.CHARACTERS.add(new Vector2i((int) vtl.x, (int) vtl.y - 1));
            DebugUILines.CHARACTERS.add(new Vector2i((int) vtl.x, (int) vbr.y));

            // RIGHT
            DebugUILines.CHARACTERS.add(new Vector2i((int) vtr.x, (int) vtr.y));
            DebugUILines.CHARACTERS.add(new Vector2i((int) vtr.x, (int) vbr.y));
        }
    }

    /*protected void renderLinePixel(UITextLine textLine, float x, float y)
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
    }*/
}
