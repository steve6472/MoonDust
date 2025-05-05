package steve6472.moondust.luau.libraries;

import steve6472.core.util.ColorUtil;
import steve6472.radiant.LuauLib;

/**
 * Created by steve6472
 * Date: 3/27/2025
 * Project: RadiantTest <br>
 */
public class ColorUtilLib extends LuauLib
{
    public static final ColorUtilLib INSTANCE = new ColorUtilLib();

    private ColorUtilLib() {}

    @SuppressWarnings("DanglingJavadoc")
    @Override
    public void createFunctions()
    {
        /// [ColorUtil#getColor(int, int, int, int)]
        addOverloadedFunc("getColor", args().number().number().number().number(), state -> {
            int r = state.checkIntegerArg(1);
            int g = state.checkIntegerArg(2);
            int b = state.checkIntegerArg(3);
            int a = state.checkIntegerArg(4);
            state.pushInteger(ColorUtil.getColor(r, g, b, a));
            return 1;
        });

        /// [ColorUtil#getColor(int, int, int)]
        addOverloadedFunc("getColor", args().number().number().number(), state -> {
            int r = state.checkIntegerArg(1);
            int g = state.checkIntegerArg(2);
            int b = state.checkIntegerArg(3);
            state.pushInteger(ColorUtil.getColor(r, g, b));
            return 1;
        });

        /// [ColorUtil#getColor(int)]
        addOverloadedFunc("getColor", args().number(), state -> {
            int gray = state.checkIntegerArg(1);
            state.pushInteger(ColorUtil.getColor(gray));
            return 1;
        });

        /// [ColorUtil#getColor(float, float, float, float)]
        addOverloadedFunc("getColor", args().number(), state -> {
            float r = (float) state.checkNumberArg(1);
            float g = (float) state.checkNumberArg(2);
            float b = (float) state.checkNumberArg(3);
            float a = (float) state.checkNumberArg(4);
            state.pushInteger(ColorUtil.getColor(r, g, b, a));
            return 1;
        });

        /// [ColorUtil#getColors(int)]
        addOverloadedFunc("getColor", args().number(), state -> {
            int color = state.checkIntegerArg(1);
            float[] colors = ColorUtil.getColors(color);
            state.pushNumber(colors[0]);
            state.pushNumber(colors[1]);
            state.pushNumber(colors[2]);
            state.pushNumber(colors[3]);
            return 4;
        });

        /// [ColorUtil#getRed(int)]
        addOverloadedFunc("getColor", args().number(), state -> {
            int color = state.checkIntegerArg(1);
            state.pushInteger(ColorUtil.getRed(color));
            return 1;
        });

        /// [ColorUtil#getGreen(int)]
        addOverloadedFunc("getColor", args().number(), state -> {
            int color = state.checkIntegerArg(1);
            state.pushInteger(ColorUtil.getGreen(color));
            return 1;
        });

        /// [ColorUtil#getBlue(int)]
        addOverloadedFunc("getColor", args().number(), state -> {
            int color = state.checkIntegerArg(1);
            state.pushInteger(ColorUtil.getBlue(color));
            return 1;
        });

        /// [ColorUtil#getAlpha(int)]
        addOverloadedFunc("getColor", args().number(), state -> {
            int color = state.checkIntegerArg(1);
            state.pushInteger(ColorUtil.getAlpha(color));
            return 1;
        });
    }

    @Override
    public String name()
    {
        return "ColorUtil";
    }
}
