package steve6472.moondust;

import com.mojang.serialization.Codec;

import java.util.List;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public class MoonDustCodecs
{
    public static final Codec<List<String>> PATH = Codec.STRING.xmap(s -> List.of(s.split("\\.")), list -> String.join(".", list));
}
