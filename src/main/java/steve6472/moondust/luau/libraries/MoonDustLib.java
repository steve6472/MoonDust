package steve6472.moondust.luau.libraries;

import steve6472.core.registry.Key;
import steve6472.flare.registry.FlareRegistries;
import steve6472.flare.ui.font.render.TextPart;
import steve6472.flare.ui.font.style.FontStyleEntry;
import steve6472.moondust.MoonDust;
import steve6472.moondust.widget.Widget;
import steve6472.moondust.widget.component.MDText;
import steve6472.radiant.LuauLib;

import java.util.Optional;

/**
 * Created by steve6472
 * Date: 5/5/2025
 * Project: MoonDust <br>
 */
public class MoonDustLib extends LuauLib
{
    public static final MoonDustLib INSTANCE = new MoonDustLib();

    private MoonDustLib() {}

    @Override
    public void createFunctions()
    {
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
    }

    @Override
    public String name()
    {
        return "MoonDust";
    }
}
