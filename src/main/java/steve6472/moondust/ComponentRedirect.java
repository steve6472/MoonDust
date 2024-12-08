package steve6472.moondust;

import steve6472.moondust.widget.component.position.AbsolutePos;
import steve6472.moondust.widget.component.position.AnchoredPos;
import steve6472.moondust.widget.component.position.Position;
import steve6472.moondust.widget.component.position.RelativePos;

import java.util.*;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 */
public final class ComponentRedirect
{
    private static final Map<Class<?>, Collection<Class<?>>> redirects = new HashMap<>();

    static
    {
        addRedirect(Position.class, AbsolutePos.class, RelativePos.class, AnchoredPos.class);
    }

    public static Collection<Class<?>> get(Class<?> objectType)
    {
        return redirects.get(objectType);
    }

    public static void addRedirect(Class<?> interfaceType, Class<?> objectType)
    {
        if (objectType.isInterface())
            throw new RuntimeException("Object type is an interface, this is not allowed.");
        if (!interfaceType.isInterface())
            throw new RuntimeException("Interface type is not interface, this is not allowed.");

        redirects.computeIfAbsent(interfaceType, _ -> new HashSet<>()).add(objectType);
    }

    public static void addRedirect(Class<?> interfaceType, Class<?>... objectTypes)
    {
        for (Class<?> objectType : objectTypes)
        {
            addRedirect(interfaceType, objectType);
        }
    }
}
