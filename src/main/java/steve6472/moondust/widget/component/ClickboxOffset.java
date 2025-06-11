package steve6472.moondust.widget.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector2i;
import steve6472.core.util.ExtraCodecs;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
public class ClickboxOffset
{
    private static final Codec<ClickboxOffset> CODEC_XY = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("x").forGetter(o -> o.x),
        Codec.INT.fieldOf("y").forGetter(o -> o.y)
    ).apply(instance, ClickboxOffset::new));

    private static final Codec<ClickboxOffset> CODEC_ARR = ExtraCodecs.VEC_2I.xmap(v -> new ClickboxOffset(v.x, v.y), o -> new Vector2i(o.x, o.y));

    public static final Codec<ClickboxOffset> CODEC = Codec.withAlternative(CODEC_ARR, CODEC_XY);

    public int x, y;

    public ClickboxOffset(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
