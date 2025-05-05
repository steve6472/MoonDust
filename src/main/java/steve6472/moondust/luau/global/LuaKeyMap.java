package steve6472.moondust.luau.global;

import steve6472.radiant.LuauTable;

/**
 * Created by steve6472
 * Date: 3/29/2025
 * Project: RadiantTest <br>
 */
public class LuaKeyMap
{
    public static final LuauTable KEYS = new LuauTable();

    public static void setup()
    {
        // Add each key name and its corresponding keycode
        KEYS.add("A", 65);
        KEYS.add("B", 66);
        KEYS.add("C", 67);
        KEYS.add("D", 68);
        KEYS.add("E", 69);
        KEYS.add("F", 70);
        KEYS.add("G", 71);
        KEYS.add("H", 72);
        KEYS.add("I", 73);
        KEYS.add("J", 74);
        KEYS.add("K", 75);
        KEYS.add("L", 76);
        KEYS.add("M", 77);
        KEYS.add("N", 78);
        KEYS.add("O", 79);
        KEYS.add("P", 80);
        KEYS.add("Q", 81);
        KEYS.add("R", 82);
        KEYS.add("S", 83);
        KEYS.add("T", 84);
        KEYS.add("U", 85);
        KEYS.add("V", 86);
        KEYS.add("W", 87);
        KEYS.add("X", 88);
        KEYS.add("Y", 89);
        KEYS.add("Z", 90);

        KEYS.add("Num0", 48);
        KEYS.add("Num1", 49);
        KEYS.add("Num2", 50);
        KEYS.add("Num3", 51);
        KEYS.add("Num4", 52);
        KEYS.add("Num5", 53);
        KEYS.add("Num6", 54);
        KEYS.add("Num7", 55);
        KEYS.add("Num8", 56);
        KEYS.add("Num9", 57);

        KEYS.add("Space", 32);
        KEYS.add("Enter", 13);
        KEYS.add("Escape", 27);
        KEYS.add("Backspace", 8);
        KEYS.add("Tab", 9);
        KEYS.add("Shift", 16);
        KEYS.add("Control", 17);
        KEYS.add("Alt", 18);

        KEYS.add("ArrowUp", 38);
        KEYS.add("ArrowDown", 40);
        KEYS.add("ArrowLeft", 37);
        KEYS.add("ArrowRight", 39);

        KEYS.add("F1", 112);
        KEYS.add("F2", 113);
        KEYS.add("F3", 114);
        KEYS.add("F4", 115);
        KEYS.add("F5", 116);
        KEYS.add("F6", 117);
        KEYS.add("F7", 118);
        KEYS.add("F8", 119);
        KEYS.add("F9", 120);
        KEYS.add("F10", 121);
        KEYS.add("F11", 122);
        KEYS.add("F12", 123);

        KEYS.add("LeftBracket", 91);
        KEYS.add("RightBracket", 93);
        KEYS.add("Comma", 44);
        KEYS.add("Period", 46);
        KEYS.add("Slash", 47);
        KEYS.add("Semicolon", 59);
        KEYS.add("Quote", 39);
        KEYS.add("Backslash", 92);
        KEYS.add("Equals", 61);
        KEYS.add("Minus", 45);
    }
}
