package steve6472.moondust.blueprints;

import steve6472.core.registry.Key;
import steve6472.core.registry.Type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import steve6472.moondust.MoonDustConstants;
import steve6472.moondust.MoonDustRegistries;
import steve6472.moondust.blueprints.values.BlueprintValueBool;
import steve6472.moondust.blueprints.values.BlueprintValueInt;
import steve6472.moondust.blueprints.values.BlueprintValueString;
import steve6472.moondust.blueprints.values.BlueprintValueTable;

import java.util.Map;

public final class BlueprintValueType<V, T extends BlueprintValue<V>> extends Type<T>
{
    public static final BlueprintValueType<Integer, ?> INT = register("int", BlueprintValueInt.CODEC);
    public static final BlueprintValueType<String, ?> STRING = register("string", BlueprintValueString.CODEC);
    public static final BlueprintValueType<Boolean, ?> BOOL = register("bool", BlueprintValueBool.CODEC);
    public static final BlueprintValueType<Map<String, BlueprintValue<?>>, ?> TABLE = register("table", BlueprintValueTable.CODEC);

    public BlueprintValueType(Key key, MapCodec<T> codec)
    {
        super(key, codec);
    }

    private static <V, T extends BlueprintValue<V>> BlueprintValueType<V, T> register(String id, Codec<T> codec)
    {
        var obj = new BlueprintValueType<>(MoonDustConstants.key(id), MapCodec.assumeMapUnsafe(codec));
        MoonDustRegistries.BLUEPRINT_VALUE_TYPE.register(obj);
        return obj;
    }
}
