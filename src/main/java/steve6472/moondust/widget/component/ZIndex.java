package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;

/**
 * Created by steve6472
 * Date: 12/19/2024
 * Project: MoonDust <br>
 */
public class ZIndex
{
    public float zIndex;

    public static final Codec<ZIndex> CODEC = Codec.DOUBLE.xmap(d -> new ZIndex(d.floatValue()), z -> (double) z.zIndex);

    public ZIndex(float zIndex)
    {
        this.zIndex = zIndex;
    }
}
