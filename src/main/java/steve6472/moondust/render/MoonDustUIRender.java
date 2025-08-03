package steve6472.moondust.render;

import com.mojang.datafixers.util.Pair;
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
import steve6472.flare.core.FlareApp;
import steve6472.flare.input.UserInput;
import steve6472.flare.render.impl.UIRenderImpl;
import steve6472.flare.ui.textures.SpriteEntry;
import steve6472.moondust.MoonDust;
import steve6472.moondust.widget.Panel;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.blueprint.RenderOrderBlueprint;
import steve6472.moondust.widget.component.*;
import steve6472.moondust.widget.component.event.*;

import java.util.*;
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

    private final Window window;
    private final UserInput input;

    private final List<WidgetSpritePrimitive> primitives = new ArrayList<>();
    Widget pressedWidget = null;
    boolean canInteract = true;

    public MoonDustUIRender(FlareApp main)
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
        }

        long window = this.window.window();
        boolean leftPress = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

        // Handles the "pressed a component but drag the mouse away from it"
        if (pressedWidget != null)
        {
            if (!pressedWidget.isEnabled())
            {
                pressedWidget.internalStates().directHover = false;
                pressedWidget.internalStates().hovered = false;
                pressedWidget = null;
            } else
            {
                pressedWidget.getClickboxSize().ifPresent(clickboxSize -> {
                    Vector2i clickboxPosition = pressedWidget.getPosition();
                    pressedWidget.getComponent(ClickboxOffset.class).ifPresent(offset -> clickboxPosition.add(offset.x, offset.y));
                    boolean hovered = isInRectangle(clickboxPosition.x, clickboxPosition.y, clickboxPosition.x + clickboxSize.width(), clickboxPosition.y + clickboxSize.height(), mousePos.x, mousePos.y);
                    pressedWidget.internalStates().directHover = hovered;
                    if (leftPress)
                        return;

                    pressedWidget.handleEvents(OnMouseRelease.class, event -> event.cursorInside().test(hovered));
                });

                if (!leftPress)
                {
                    pressedWidget = null;
                }
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

        sortAndRenderPrimitives();

        primitives.clear();
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

        widget.getClickboxSize().ifPresent(clickboxSize ->
        {
            Vector2i clickboxPosition = widget.getPosition();
            widget.getComponent(ClickboxOffset.class).ifPresent(offset -> clickboxPosition.add(offset.x, offset.y));
            boolean hovered = isInRectangle(clickboxPosition.x, clickboxPosition.y, clickboxPosition.x + clickboxSize.width(), clickboxPosition.y + clickboxSize.height(), mousePos.x, mousePos.y);

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
        widget.handleEvents(OnRandomTick.class, event -> RandomUtil.decide(event.probability()));

        widget.getComponents(CurrentSprite.class, Sprites.class).ifPresent(result ->
        {
            Key sprite = getSprite(result.comp1(), result.comp2());

            widget.getSpriteSize().ifPresent(spriteSize ->
            {
                Vector2i position = widget.getPosition();
                widget.getComponent(SpriteOffset.class).ifPresent(offset -> position.add(offset.x, offset.y));
                sprite(position.x, position.y, spriteSize.width(), spriteSize.height(), sprite, widget);
            });
        });

        if (widget.isFocusable() && widget.internalStates().focused)
        {
            widget.getSpriteSize().ifPresent(spriteSize ->
            {
                Vector2i position = widget.getPosition();
                widget.getComponent(SpriteOffset.class).ifPresent(offset -> position.add(offset.x, offset.y));
                widget.getComponent(FocusedSprite.class).ifPresentOrElse(focusedSprite ->
                {
                    SpriteEntry textureEntry = getTextureEntry(focusedSprite.sprite());
                    if (textureEntry == null)
                        sprite(position.x, position.y, spriteSize.width(), spriteSize.height(), MoonDust.ERROR_FOCUSED, widget);
                    else
                        sprite(position.x, position.y, spriteSize.width(), spriteSize.height(), focusedSprite.sprite(), widget);
                }, () -> {
                    sprite(position.x, position.y, spriteSize.width(), spriteSize.height(), MoonDust.ERROR_FOCUSED, widget);
                });
            });
        }
    }

    private int findIndexOf(Widget widget, Pair<RenderOrder, WidgetSpritePrimitive> order)
    {
        // Find the widget to be ordered by
        List<WidgetSpritePrimitive> orderByList = primitives.stream()
            // Verify they are both in the same widget
            .filter(primitive ->
            {
                Optional<Widget> primitiveParent = primitive.widget().parent();
                Optional<Widget> orderParent = widget.parent();
                if (primitiveParent.isEmpty() && orderParent.isEmpty())
                    return true;
                if (primitiveParent.isEmpty() ^ orderParent.isEmpty())
                    return false;

                return primitiveParent.get() == orderParent.get();
            })
            // Filter widget by name
            .filter(primitive -> primitive.widget().getName().equals(order.getFirst().widget()))
            .toList();

        if (orderByList.size() != 1)
            throw new RuntimeException("orderByList size is not 1, it is " + orderByList.size());

        WidgetSpritePrimitive orderByPrimitive = orderByList.getFirst();
        return primitives.indexOf(orderByPrimitive);
    }

    protected final void sortAndRenderPrimitives()
    {
        float pixelScale = MoonDust.getInstance().getPixelScale();
        float zIndex = 0;
        final float delta = 1f / 2560f;

        // Has to be a list because focus sprite exists...
        Map<Widget, List<Pair<RenderOrder, WidgetSpritePrimitive>>> orders = new LinkedHashMap<>();

        for (WidgetSpritePrimitive primitive : primitives)
        {
            primitive.widget().getComponent(RenderOrder.class).ifPresent(ro -> {
                orders.computeIfAbsent(primitive.widget(), _ -> new ArrayList<>()).add(Pair.of(ro, primitive));
            });
        }

        orders.forEach((widget, orderList) ->
        {
            // Remove the widget
            primitives.removeIf(primitive -> primitive.widget() == widget);

            // Some ugly hack because focus exists
            // (in case one widget can render multiple sprites)
            int lastIndex = -1;

            for (Pair<RenderOrder, WidgetSpritePrimitive> order : orderList)
            {
                if (lastIndex == -1)
                {
                    lastIndex = findIndexOf(widget, order);
                    lastIndex += (order.getFirst().order() == RenderOrderBlueprint.Order.BELOW ? 0 : 1);
                } else
                {
                    lastIndex++;
                }
                primitives.add(lastIndex, order.getSecond());
            }
        });

        // Reverse the order because sprite z index is weird lul
        Collections.reverse(primitives);

        for (WidgetSpritePrimitive primitive : primitives)
        {
            createSprite(
                (int) (primitive.x() * pixelScale),
                (int) (primitive.y() * pixelScale),
                zIndex,
                (int) (primitive.w() * pixelScale),
                (int) (primitive.h() * pixelScale),
                primitive.w(),
                primitive.h(),
                NO_TINT,
                getTextureEntry(primitive.texture()));

            zIndex -= delta;
        }
    }

    protected Key getSprite(CurrentSprite currentSprite, Sprites sprites)
    {
        String sprite = currentSprite.sprite();
        Key spriteKey = sprites.sprites().get(sprite);
        if (spriteKey == null)
        {
            Log.warningOnce(LOGGER, "Missing Sprite '%s'".formatted(sprite));
            return FlareConstants.ERROR_TEXTURE;
        }
        return spriteKey;
    }

    protected final void sprite(
        int x, int y,
        int width, int height,
        Key textureKey, Widget widget)
    {
        float pixelScale = MoonDust.getInstance().getPixelScale();
//        createSprite((int) (x * pixelScale), (int) (y * pixelScale), zIndex, (int) (width * pixelScale), (int) (height * pixelScale), width, height, NO_TINT, getTextureEntry(textureKey));
        primitives.add(new WidgetSpritePrimitive(x, y, width, height, textureKey, widget));
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
