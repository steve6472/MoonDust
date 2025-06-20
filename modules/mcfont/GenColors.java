import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

class GenColors
{
    static String STYLE = """
        {
             font: "mcfont:bold_italic",
            base: {
                color: [%r%, %g%, %b%, 1.0],
                softness: 0.0,
                thickness: 0.5
            },
            outline: {
                color: [1.0, 1.0, 1.0, 1.0],
                softness: 0.0,
                thickness: 0.0
            },
            shadow: {
                color: [%sr%, %sg%, %sb%, 1.0],
                softness: 0.0,
                thickness: 0.5
            },
            shadow_offset: [0.1, 0.1]
        }""";

    private static List<String> NAMES = List.of("black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple", "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow", "white");
    private static List<Integer> COLORS = List.of(0x000000, 0x0000aa, 0x00aa00, 0x00aaaa, 0xaa0000, 0xaa00aa, 0xffaa00, 0xaaaaaa, 0x555555, 0x5555ff, 0x55ff55, 0x55ffff, 0xff5555, 0xff55ff, 0xffff55, 0xffffff);

    public static int getRed(int color)
    {
        return (color >> 16) & 0xff;
    }

    public static int getGreen(int color)
    {
        return (color >> 8) & 0xff;
    }

    public static int getBlue(int color)
    {
        return color & 0xff;
    }

    public static void main(String[] args) throws IOException
    {
        final float dumbShading = 0.9882352941176471f;
        final float shadow = 0.2470588235294118f;
        int i = 0;
        for (Integer color : COLORS)
        {
            float r = (getRed(color) / 255f);
            float g = (getGreen(color) / 255f);
            float b = (getBlue(color) / 255f);
            String json = STYLE
                .replaceAll("%r%", Float.toString(r * dumbShading))
                .replaceAll("%g%", Float.toString(g * dumbShading))
                .replaceAll("%b%", Float.toString(b * dumbShading))
                .replaceAll("%sr%", Float.toString(r * dumbShading * shadow))
                .replaceAll("%sg%", Float.toString(g * dumbShading * shadow))
                .replaceAll("%sb%", Float.toString(b * dumbShading * shadow));
            
            PrintWriter writer = new PrintWriter(NAMES.get(i) + ".json5", StandardCharsets.UTF_8);
            for (String s : json.split("\n"))
            {
                writer.println(s);
            }
            writer.close();

            i++;
        }
    }
}