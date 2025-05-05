package steve6472.moondust;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;
import steve6472.radiant.LuaTableOps;
import steve6472.radiant.LuauMetaTable;

import java.util.Optional;

/**
 * Created by steve6472
 * Date: 3/31/2025
 * Project: MoonDust <br>
 */
public record ComponentEntry(Key key, Codec<?> codec, Class<?> clazz, Optional<LuauMetaTable> meta) implements Keyable
{
    public Object toLua(Object object)
    {
        //noinspection unchecked,rawtypes
        DataResult<Object> objectDataResult = ((Codec) codec).encodeStart(LuaTableOps.INSTANCE, object);
        return objectDataResult.getOrThrow();
    }
}
