package steve6472.moondust.widget.component.event;

import steve6472.moondust.core.Mergeable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve6472
 * Date: 12/9/2024
 * Project: MoonDust <br>
 */
public record UIEvents(List<UIEventCallEntry> events) implements Mergeable<UIEvents>
{
    @Override
    public UIEvents merge(UIEvents left, UIEvents right)
    {
        ArrayList<UIEventCallEntry> list = new ArrayList<>();
        list.addAll(left.events);
        list.addAll(right.events);
        return new UIEvents(list);
    }
}
