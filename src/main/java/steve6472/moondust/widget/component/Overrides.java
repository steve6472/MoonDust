package steve6472.moondust.widget.component;

import steve6472.moondust.core.Mergeable;
import steve6472.moondust.widget.BlueprintOverride;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 12/11/2024
 * Project: MoonDust <br>
 */
public record Overrides(List<BlueprintOverride<?>> overrides) implements Mergeable<Overrides>
{
    @Override
    public Overrides merge(Overrides left, Overrides right)
    {
        List<BlueprintOverride<?>> list = new ArrayList<>(left.overrides);
        list.addAll(right.overrides);
        return new Overrides(list);
    }
}
