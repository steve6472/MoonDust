package steve6472.moondust.widget.blueprint.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import steve6472.core.registry.Key;
import steve6472.core.registry.Type;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public final class PositionBlueprintType<T extends PositionBlueprint> extends Type<T>
{
    public static final PositionBlueprintType<AbsolutePosBlueprint> ABSOLUTE = register("absolute", AbsolutePosBlueprint.CODEC);
    public static final PositionBlueprintType<RelativePosBlueprint> RELATIVE = register("relative", RelativePosBlueprint.CODEC);
    public static final PositionBlueprintType<AnchoredPosBlueprint> ANCHORED = register("anchored", AnchoredPosBlueprint.CODEC);

    public PositionBlueprintType(Key key, MapCodec<T> codec)
    {
        super(key, codec);
    }

    private static <T extends PositionBlueprint> PositionBlueprintType<T> register(String id, Codec<T> codec)
    {
        var obj = new PositionBlueprintType<>(MoonDustConstants.key(id), MapCodec.assumeMapUnsafe(codec));
        MoonDustRegistries.POSITION_BLUEPRINT_TYPE.register(obj);
        return obj;
    }
}
