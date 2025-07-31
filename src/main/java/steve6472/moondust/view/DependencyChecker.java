package steve6472.moondust.view;

import steve6472.core.log.Log;
import steve6472.moondust.view.exp.Expression;
import steve6472.moondust.view.property.Property;

import java.util.*;
import java.util.logging.Logger;

public class DependencyChecker
{
    private static final Logger LOGGER = Log.getLogger(DependencyChecker.class);

    private final Set<Property<?>> visited = new HashSet<>();
    private final Deque<Property<?>> path = new ArrayDeque<>();

    public void check(Property<?> property)
    {
        visited.clear();
        path.clear();
        detectCycle(property);
    }

    private void detectCycle(Property<?> property)
    {
        if (path.contains(property))
        {
            logCycle(property);
            throw new IllegalStateException("Circular dependency detected involving: " + property.getDebugName());
        }

        if (visited.contains(property))
        {
            return;
        }

        path.push(property);
        Expression<?> binder = property.getBinder();

        if (binder != null)
        {
            for (Property<?> dep : binder.getDependencies())
            {
                detectCycle(dep);
            }
        }

        path.pop();
        visited.add(property);
    }

    private void logCycle(Property<?> start)
    {
        StringBuilder sb = new StringBuilder("Circular dependency detected:  ");
        boolean passedFirst = false;
        for (Property<?> p : path)
        {
            if (passedFirst)
                sb.append(" -> ");
            passedFirst = true;
            sb.append(p.getDebugName());
        }
        sb.append(" -> ").append(start.getDebugName()).append(" (back to start)");
        LOGGER.severe(sb.toString());
    }
}