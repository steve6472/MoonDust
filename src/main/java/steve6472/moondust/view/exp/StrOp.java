package steve6472.moondust.view.exp;

import steve6472.moondust.view.property.Property;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
@SuppressWarnings("unused")
public interface StrOp
{
    static Exp<Integer> len(Property<String> str) { return () -> str.get().length(); }

    @SafeVarargs
    static Exp<String> conc(Property<String>... vals)
    {
        return () -> {
            StringBuilder bob = new StringBuilder();
            for (Property<String> val : vals)
            {
                bob.append(val.get());
            }
            return bob.toString();
        };
    }
}
