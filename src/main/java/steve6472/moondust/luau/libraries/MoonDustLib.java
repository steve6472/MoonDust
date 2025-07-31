package steve6472.moondust.luau.libraries;

import steve6472.core.log.Log;
import steve6472.core.registry.Key;
import steve6472.flare.registry.FlareRegistries;
import steve6472.flare.ui.font.render.TextPart;
import steve6472.flare.ui.font.render.TextRenderSegment;
import steve6472.flare.ui.font.style.FontStyleEntry;
import steve6472.flare.util.FloatUtil;
import steve6472.moondust.MoonDust;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.core.JavaFunc;
import steve6472.moondust.core.blueprint.BlueprintFactory;
import steve6472.moondust.luau.global.LuaWidget;
import steve6472.moondust.widget.Panel;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.MDText;
import steve6472.moondust.widget.component.event.OnInit;
import steve6472.radiant.LuaTableOps;
import steve6472.radiant.LuauLib;
import steve6472.radiant.LuauTable;
import steve6472.radiant.LuauUtil;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by steve6472
 * Date: 5/5/2025
 * Project: MoonDust <br>
 */
public class MoonDustLib extends LuauLib
{
    private static final Logger LOGGER = Log.getLogger(MoonDustLib.class);
    public static final MoonDustLib INSTANCE = new MoonDustLib();

    private MoonDustLib() {}

    @Override
    public void createFunctions()
    {
        addFunction("runJavaFunc", state -> {
            String key = state.checkStringArg(1);
            Key parsedKey = Key.parse(MoonDustConstants.NAMESPACE, key);
            Object input = null;
            if (state.getTop() == 2)
                input = LuauUtil.toJava(state, 2);
            JavaFunc javaFunc = MoonDustRegistries.JAVA_FUNC.get(parsedKey);
            if (javaFunc == null)
            {
                Log.warningOnce(LOGGER, "Could not find java function '%s'".formatted(parsedKey));
                state.pushNil();
                return 1;
            }
            Object o = javaFunc.runFunction(input);
            LuauUtil.push(state, o);
            return 1;
        });

        addFunction("getPixelScale", state -> {
            state.pushNumber(MoonDust.getInstance().getPixelScale());
            return 1;
        });

        addFunction("setPixelScale", state -> {
            double scale = state.checkNumberArg(1);
            MoonDust.getInstance().setPixelScale((float) scale);
            return 0;
        });

        addFunction("clearFocus", _ -> {
            MoonDust.getInstance().clearFocus();
            return 0;
        });

        addFunction("focus", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            MoonDust.getInstance().focus(widget);
            return 0;
        });

        addFunction("addPanel", state -> {
            String key = state.checkStringArg(1);
            Key parsedKey = Key.parse(key);
            BlueprintFactory blueprintFactory = MoonDustRegistries.WIDGET_FACTORY.get(parsedKey);
            if (blueprintFactory == null)
            {
                LOGGER.warning("Panel '%s' not found, can not add".formatted(parsedKey));
                state.pushNil();
                return 1;
            }
            Panel panel = Panel.create(blueprintFactory);
            MoonDust.getInstance().addPanel(panel);
            LuaWidget.createObject(panel).pushUserObject(state);
            return 1;
        });

        addFunction("removePanel", state -> {
            String key = state.checkStringArg(1);
            Key parsedKey = Key.parse(key);

            Optional<Panel> first = MoonDust
                .getInstance()
                .getPanels()
                .stream()
                .filter(panel -> panel.getKey().equals(parsedKey))
                .findFirst();
            first.ifPresentOrElse(panel -> MoonDust.getInstance().removePanel(panel), () -> LOGGER.warning("Panel '%s' not found, can not remove".formatted(parsedKey)));
            return 0;
        });

        addFunction("getPanel", state -> {
            String key = state.checkStringArg(1);
            Key parsedKey = Key.parse(key);

            Optional<Panel> first = MoonDust
                .getInstance()
                .getPanels()
                .stream()
                .filter(panel -> panel.getKey().equals(parsedKey))
                .findFirst();

            if (first.isPresent())
            {
                LuaWidget.createObject(first.get()).pushUserObject(state);
            } else
            {
                state.pushNil();
            }
            return 1;
        });

        /*
         * Stuff I'm too lazy to replace with proper lua yet
         */

        // replaceStyle(widget, replacement)
        addFunction("replaceStyleText", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            Key styleKey = Key.parse(state.checkStringArg(2));

            widget.getComponent(MDText.class).ifPresent(mdLine -> {
                TextPart textPart = mdLine.text().parts().get(0);
                FontStyleEntry styleEntry = FlareRegistries.FONT_STYLE.get(styleKey);
                mdLine.replaceText(new TextPart(textPart.text(), textPart.size(), styleEntry), 0);
            });
            return 0;
        });

        // replaceStyle(widget, replacement)
        addFunction("replaceStyle", state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            Key styleKey = Key.parse(state.checkStringArg(2));

            widget.getChild("label").ifPresent(child -> {
                Optional<MDText> component = child.getComponent(MDText.class);
                component.ifPresent(mdLine -> {
                    TextPart textPart = mdLine.text().parts().get(0);
                    FontStyleEntry styleEntry = FlareRegistries.FONT_STYLE.get(styleKey);
                    mdLine.replaceText(new TextPart(textPart.text(), textPart.size(), styleEntry), 0);
                });
            });
            return 0;
        });

        // replaceText(widget, replacement)
        addOverloadedFunc("replaceText", args().user("Widget").string(), state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            widget.getComponent(MDText.class).ifPresent(text -> {
                String replacement = state.checkStringArg(2);
                text.replaceText(replacement, 0);
            });
            return 0;
        });

        // replaceText(widget, replacement, index)
        addOverloadedFunc("replaceText", args().user("Widget").string().number(), state -> {
            Widget widget = (Widget) state.checkUserDataArg(1, "Widget");
            widget.getComponent(MDText.class).ifPresent(text -> {
                String replacement = state.checkStringArg(2);
                int index = state.checkIntegerArg(3);
                text.replaceText(replacement, index);
            });
            return 0;
        });

        // getTextTotalFontHeight("moondust:text" table)
        addOverloadedFunc("getTextTotalFontHeight", args().table(), state -> {
            LuauTable table = new LuauTable();
            table.readTable(state, 1);
            var decode = MDText.CODEC.decode(LuaTableOps.INSTANCE, table);
            if (decode.isError())
            {
                state.pushNumber(0);
                return 1;
            }
            MDText text = decode.getOrThrow().getFirst();
            List<TextRenderSegment> segments = text.text().createSegments();
            final float totalHeight = segments.stream().map(s -> s.fontHeight).collect(FloatUtil.summing(Float::floatValue));
            state.pushNumber(totalHeight);
            return 1;
        });

        // getTextTotalCharHeight("moondust:text" table)
        addOverloadedFunc("getTextTotalCharHeight", args().table(), state -> {
            LuauTable table = new LuauTable();
            table.readTable(state, 1);
            var decode = MDText.CODEC.decode(LuaTableOps.INSTANCE, table);
            if (decode.isError())
            {
                state.pushNumber(0);
                return 1;
            }
            MDText text = decode.getOrThrow().getFirst();
            List<TextRenderSegment> segments = text.text().createSegments();
            float totalHeight = 0f;

            for (int i = 0; i < segments.size(); i++)
            {
                TextRenderSegment s = segments.get(i);
                final float[] maxHeight = {0f};

                if (i == segments.size() - 1)
                {
                    text.text().iterateCharacters(s.start, s.end, c ->
                    {
                        float height = c.glyph().planeBounds().height();
                        if (height > maxHeight[0])
                        {
                            maxHeight[0] = height * c.size();
                        }
                    });
                } else
                {
                    maxHeight[0] = s.fontHeight;
                }

                totalHeight += maxHeight[0];
            }
            state.pushNumber(totalHeight);
            return 1;
        });

        // getTextLineCount("moondust:text" table)
        addOverloadedFunc("getTextLineCount", args().table(), state -> {
            LuauTable table = new LuauTable();
            table.readTable(state, 1);
            var decode = MDText.CODEC.decode(LuaTableOps.INSTANCE, table);
            if (decode.isError())
            {
                state.pushNumber(0);
                return 1;
            }
            MDText text = decode.getOrThrow().getFirst();
            List<TextRenderSegment> segments = text.text().createSegments();
            state.pushInteger(segments.size());
            return 1;
        });

        // getTextMaxWidth("moondust:text" table)
        addOverloadedFunc("getTextMaxWidth", args().table(), state -> {
            LuauTable table = new LuauTable();
            table.readTable(state, 1);
            var decode = MDText.CODEC.decode(LuaTableOps.INSTANCE, table);
            if (decode.isError())
            {
                state.pushNumber(0);
                return 1;
            }
            MDText text = decode.getOrThrow().getFirst();
            float width = text.text().getWidth(0, text.text().len());
            state.pushNumber(width);
            return 1;
        });
    }

    @Override
    public String name()
    {
        return "MoonDust";
    }
}
