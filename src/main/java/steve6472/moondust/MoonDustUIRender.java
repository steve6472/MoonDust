package steve6472.moondust;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.core.util.RandomUtil;
import steve6472.flare.FlareConstants;
import steve6472.flare.Window;
import steve6472.flare.input.UserInput;
import steve6472.flare.render.impl.UIRenderImpl;
import steve6472.flare.ui.font.render.UITextLine;
import steve6472.flare.ui.textures.SpriteEntry;
import steve6472.moondust.widget.Panel;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.blueprint.event.condition.Tristate;
import steve6472.moondust.widget.component.*;
import steve6472.moondust.widget.component.event.*;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 12/15/2024
 * Project: MoonDust <br>
 */
public class MoonDustUIRender extends UIRenderImpl
{
    private static final Logger LOGGER = Log.getLogger(MoonDustUIRender.class);
    private static final boolean DEBUG_CURSOR = true;
    private static final Set<String> MISSING_SPRITES = new HashSet<>(16);

    private final Window window;
    private final UserInput input;

    Widget pressedWidget = null;
    boolean canInteract = true;

    public MoonDustUIRender(MoonDustTest main)
    {
        this.window = main.window();
        this.input = main.input();
    }

    @Override
    public void render()
    {
        MoonDust moonDust = MoonDust.getInstance();
        float pixelScale = moonDust.getPixelScale();
        moonDust.iterate((depth, widget) ->
        {
            if (depth == 0 && widget instanceof Panel panel)
            {
                panel.setBounds((int) (window.getWidth() / pixelScale), (int) (window.getHeight() / pixelScale));
            }
        });

        moonDust.tickFocus();

        Vector2i mousePos = input.getMousePositionRelativeToTopLeftOfTheWindow();
        mousePos.div(pixelScale);

        if (DEBUG_CURSOR)
        {
            sprite(mousePos.x, mousePos.y, 0, 1, 1, new Vector3f(0, 1, 0), Key.withNamespace("moondust","sprites/pixel"));
            moonDust.iterate((depth, widget) -> {
                if (depth != 0)
                    return;

                widget.getChild("debug_label").ifPresent(label -> {
                    label.getComponent(MDTextLine.class)
                        .ifPresent(mdTextLine -> label.addComponent(new MDTextLine(new UITextLine("Cursor: " + mousePos, mdTextLine.line().size(), mdTextLine.line().style(), mdTextLine.line().anchor()), mdTextLine.offset())));
                });
            });
        }

        long window = this.window.window();
        boolean leftPress = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

        // Handles the "pressed a component but drag the mouse away from it"
        if (pressedWidget != null)
        {
            pressedWidget.getComponent(ClickboxSize.class).ifPresent(clickboxSize -> {
                Vector2i clickboxPosition = pressedWidget.getPosition();
                pressedWidget.getComponent(ClickboxOffset.class).ifPresent(offset -> clickboxPosition.add(offset.x, offset.y));
                boolean hovered = isInRectangle(clickboxPosition.x, clickboxPosition.y, clickboxPosition.x + clickboxSize.width, clickboxPosition.y + clickboxSize.height, mousePos.x, mousePos.y);
                pressedWidget.internalStates().directHover = hovered;
                if (leftPress)
                    return;

                pressedWidget.getEvents(OnMouseRelease.class).forEach(e ->
                {
                    UIEventCall<OnMouseRelease> uiEventCall = (UIEventCall<OnMouseRelease>) MoonDustRegistries.EVENT_CALLS.get(e.call());
                    if (uiEventCall != null)
                    {
                        OnMouseRelease event = (OnMouseRelease) e.event();
                        if (event.cursorInside() == Tristate.IGNORE)
                            uiEventCall.call(pressedWidget, event);
                        else if (event.cursorInside() == Tristate.TRUE && hovered)
                            uiEventCall.call(pressedWidget, event);
                        else if (event.cursorInside() == Tristate.FALSE && !hovered)
                            uiEventCall.call(pressedWidget, event);
                        else
                            throw new RuntimeException("Unexpected state for OnMouseRelease call!");
                    } else
                    {
                        LOGGER.warning("No event call found for " + e.call());
                    }
                });
            });

            if (!leftPress)
            {
                pressedWidget = null;
            }
        }

        if (!leftPress)
            canInteract = true;

        moonDust.iterate(widget ->
        {
            processEvents(widget);
            renderWidget(widget);
        });

        if (leftPress)
            canInteract = false;
    }

    private static boolean isInRectangle(int rminx, int rminy, int rmaxx, int rmaxy, int px, int py)
    {
        return px >= rminx && px < rmaxx && py >= rminy && py < rmaxy;
    }

    private void processEvents(Widget widget)
    {
        if (!canInteract)
            return;

        if (!widget.isVisible() || !widget.isClickable())
            return;

        MoonDust moonDust = MoonDust.getInstance();
        float pixelScale = moonDust.getPixelScale();

        Vector2i mousePos = input.getMousePositionRelativeToTopLeftOfTheWindow();
        mousePos.div(pixelScale);

        long window = this.window.window();
        boolean leftPress = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

        widget.getComponent(ClickboxSize.class).ifPresent(clickboxSize ->
        {
            Vector2i clickboxPosition = widget.getPosition();
            widget.getComponent(ClickboxOffset.class).ifPresent(offset -> clickboxPosition.add(offset.x, offset.y));
            boolean hovered = isInRectangle(clickboxPosition.x, clickboxPosition.y, clickboxPosition.x + clickboxSize.width, clickboxPosition.y + clickboxSize.height, mousePos.x, mousePos.y);

            widget.internalStates().directHover = hovered;
            // Hover events
            if (widget.internalStates().hovered != hovered && canInteract)
            {
                if (hovered)
                {
                    widget.handleEvents(OnMouseEnter.class);
                } else
                {
                    widget.handleEvents(OnMouseLeave.class);
                }
                widget.internalStates().hovered = true;
            }

            // Click events
            if (widget.internalStates().hovered && widget.isClickable())
            {
                if (leftPress && pressedWidget == null)
                {
                    pressedWidget = widget;
                    widget.handleEvents(OnMousePress.class);
                }
            }

            widget.internalStates().hovered = hovered;
        });
    }

    // TODO: correctly add zIndex, make sure font has the same but like 0.5f difference in front of widgets
    private void renderWidget(Widget widget)
    {
        if (!widget.isVisible())
            return;

        widget.handleEvents(OnRender.class);

        widget.getEvents(OnRandomTick.class).forEach(e ->
        {
            //noinspection unchecked
            UIEventCall<OnRandomTick> uiEventCall = (UIEventCall<OnRandomTick>) MoonDustRegistries.EVENT_CALLS.get(e.call());
            if (uiEventCall != null)
            {
                OnRandomTick event = (OnRandomTick) e.event();
                if (RandomUtil.decide(event.probability()))
                    uiEventCall.call(widget, event);
            } else
            {
                LOGGER.warning("No event call found for " + e.call());
            }
        });

        if (widget.isFocusable() && widget.internalStates().focused)
        {
            widget.getComponent(SpriteSize.class).ifPresent(spriteSize ->
            {
                Vector2i position = widget.getPosition();
                widget.getComponent(SpriteOffset.class).ifPresent(offset -> position.add(offset.x, offset.y));
                widget.getComponent(FocusedSprite.class).ifPresentOrElse(focusedSprite ->
                {
                    float index = widget.getComponent(ZIndex.class).map(comp -> comp.zIndex).orElse(-0.1f);
                    index += 0.01f;
                    SpriteEntry textureEntry = getTextureEntry(focusedSprite.sprite());
                    if (textureEntry == null)
                        sprite(position.x, position.y, index, spriteSize.width, spriteSize.height, MoonDust.ERROR_FOCUSED);
                    else
                        sprite(position.x, position.y, index, spriteSize.width, spriteSize.height, focusedSprite.sprite());
                }, () -> {
                    float index = widget.getComponent(ZIndex.class).map(comp -> comp.zIndex).orElse(-0.1f);
                    sprite(position.x, position.y, index, spriteSize.width, spriteSize.height, MoonDust.ERROR_FOCUSED);
                });
            });
        }

        widget.getComponents(CurrentSprite.class, Sprites.class).ifPresent(result ->
        {
            Key sprite = getSprite(result.comp1(), result.comp2());

            widget.getComponent(SpriteSize.class).ifPresent(spriteSize ->
            {
                Vector2i position = widget.getPosition();
                widget.getComponent(SpriteOffset.class).ifPresent(offset -> position.add(offset.x, offset.y));
                float index = widget.getComponent(ZIndex.class).map(comp -> comp.zIndex).orElse(-0.1f);
                sprite(position.x, position.y, index, spriteSize.width, spriteSize.height, sprite);
            });
        });
    }

    protected Key getSprite(CurrentSprite currentSprite, Sprites sprites)
    {
        String sprite = currentSprite.sprite();
        Key spriteKey = sprites.sprites().get(sprite);
        if (spriteKey == null)
        {
            if (!MISSING_SPRITES.contains(sprite))
            {
                MISSING_SPRITES.add(sprite);
                LOGGER.warning("Missing Sprite '%s'".formatted(sprite));
            }
            return FlareConstants.ERROR_TEXTURE;
        }
        return spriteKey;
    }

    protected final void sprite(
        int x, int y, float zIndex,
        int width, int height,
        Key textureKey)
    {
        float pixelScale = MoonDust.getInstance().getPixelScale();
        createSprite((int) (x * pixelScale), (int) (y * pixelScale), zIndex, (int) (width * pixelScale), (int) (height * pixelScale), width, height, NO_TINT, getTextureEntry(textureKey));
    }

    protected final void sprite(
        int x, int y, float zIndex,
        int width, int height,
        Vector3f tint, Key textureKey)
    {
        float pixelScale = MoonDust.getInstance().getPixelScale();
        createSprite((int) (x * pixelScale), (int) (y * pixelScale), zIndex, (int) (width * pixelScale), (int) (height * pixelScale), width, height, tint, getTextureEntry(textureKey));
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
