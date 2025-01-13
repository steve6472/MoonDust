package steve6472.moondust.widget.component;

import steve6472.moondust.core.Mergeable;
import steve6472.moondust.core.blueprint.BlueprintFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 12/4/2024
 * Project: MoonDust <br>
 */
public record Children(List<BlueprintFactory> children) implements Mergeable<Children>
{
    @Override
    public Children merge(Children left, Children right)
    {
        Children children = new Children(new ArrayList<>(left.children));
        children.children.addAll(right.children);
        return children;
    }
}
