package steve6472.moondust.widget;

import steve6472.moondust.core.Results;

import java.util.Optional;

/**
 * Created by steve6472
 * Date: 12/16/2024
 * Project: MoonDust <br>
 */
public interface WidgetComponentGetter
{
    <T> Optional<T> getComponent(Class<T> type);

    default <T1> Optional<Results.Result1<T1>> getComponents(Class<T1> type1)
    {
        Optional<T1> comp1 = getComponent(type1);
        return comp1.map(Results.Result1::new);
    }

    default <T1, T2> Optional<Results.Result2<T1, T2>> getComponents(Class<T1> type1, Class<T2> type2)
    {
        Optional<T1> comp1 = getComponent(type1);
        if (comp1.isEmpty()) return Optional.empty();

        Optional<T2> comp2 = getComponent(type2);
        return comp2.map(t2 -> new Results.Result2<>(comp1.get(), t2));
    }

    default <T1, T2, T3> Optional<Results.Result3<T1, T2, T3>> getComponents(Class<T1> type1, Class<T2> type2, Class<T3> type3)
    {
        Optional<T1> comp1 = getComponent(type1);
        if (comp1.isEmpty()) return Optional.empty();

        Optional<T2> comp2 = getComponent(type2);
        if (comp2.isEmpty()) return Optional.empty();

        Optional<T3> comp3 = getComponent(type3);
        return comp3.map(t3 -> new Results.Result3<>(comp1.get(), comp2.get(), t3));
    }

    default <T1, T2, T3, T4> Optional<Results.Result4<T1, T2, T3, T4>> getComponents(Class<T1> type1, Class<T2> type2, Class<T3> type3, Class<T4> type4)
    {
        Optional<T1> comp1 = getComponent(type1);
        if (comp1.isEmpty()) return Optional.empty();

        Optional<T2> comp2 = getComponent(type2);
        if (comp2.isEmpty()) return Optional.empty();

        Optional<T3> comp3 = getComponent(type3);
        if (comp3.isEmpty()) return Optional.empty();

        Optional<T4> comp4 = getComponent(type4);
        return comp4.map(t4 -> new Results.Result4<>(comp1.get(), comp2.get(), comp3.get(), t4));
    }

    default <T1, T2, T3, T4, T5> Optional<Results.Result5<T1, T2, T3, T4, T5>> getComponents(Class<T1> type1, Class<T2> type2, Class<T3> type3, Class<T4> type4, Class<T5> type5)
    {
        Optional<T1> comp1 = getComponent(type1);
        if (comp1.isEmpty()) return Optional.empty();

        Optional<T2> comp2 = getComponent(type2);
        if (comp2.isEmpty()) return Optional.empty();

        Optional<T3> comp3 = getComponent(type3);
        if (comp3.isEmpty()) return Optional.empty();

        Optional<T4> comp4 = getComponent(type4);
        if (comp4.isEmpty()) return Optional.empty();

        Optional<T5> comp5 = getComponent(type5);
        return comp5.map(t5 -> new Results.Result5<>(comp1.get(), comp2.get(), comp3.get(), comp4.get(), t5));
    }

    default <T1, T2, T3, T4, T5, T6> Optional<Results.Result6<T1, T2, T3, T4, T5, T6>> getComponents(Class<T1> type1, Class<T2> type2, Class<T3> type3, Class<T4> type4, Class<T5> type5, Class<T6> type6)
    {
        Optional<T1> comp1 = getComponent(type1);
        if (comp1.isEmpty()) return Optional.empty();

        Optional<T2> comp2 = getComponent(type2);
        if (comp2.isEmpty()) return Optional.empty();

        Optional<T3> comp3 = getComponent(type3);
        if (comp3.isEmpty()) return Optional.empty();

        Optional<T4> comp4 = getComponent(type4);
        if (comp4.isEmpty()) return Optional.empty();

        Optional<T5> comp5 = getComponent(type5);
        if (comp5.isEmpty()) return Optional.empty();

        Optional<T6> comp6 = getComponent(type6);
        return comp6.map(t6 -> new Results.Result6<>(comp1.get(), comp2.get(), comp3.get(), comp4.get(), comp5.get(), t6));
    }

    default <T1, T2, T3, T4, T5, T6, T7> Optional<Results.Result7<T1, T2, T3, T4, T5, T6, T7>> getComponents(Class<T1> type1, Class<T2> type2, Class<T3> type3, Class<T4> type4, Class<T5> type5, Class<T6> type6, Class<T7> type7)
    {
        Optional<T1> comp1 = getComponent(type1);
        if (comp1.isEmpty()) return Optional.empty();

        Optional<T2> comp2 = getComponent(type2);
        if (comp2.isEmpty()) return Optional.empty();

        Optional<T3> comp3 = getComponent(type3);
        if (comp3.isEmpty()) return Optional.empty();

        Optional<T4> comp4 = getComponent(type4);
        if (comp4.isEmpty()) return Optional.empty();

        Optional<T5> comp5 = getComponent(type5);
        if (comp5.isEmpty()) return Optional.empty();

        Optional<T6> comp6 = getComponent(type6);
        if (comp6.isEmpty()) return Optional.empty();

        Optional<T7> comp7 = getComponent(type7);
        return comp7.map(t7 -> new Results.Result7<>(comp1.get(), comp2.get(), comp3.get(), comp4.get(), comp5.get(), comp6.get(), t7));
    }

    default <T1, T2, T3, T4, T5, T6, T7, T8> Optional<Results.Result8<T1, T2, T3, T4, T5, T6, T7, T8>> getComponents(Class<T1> type1, Class<T2> type2, Class<T3> type3, Class<T4> type4, Class<T5> type5, Class<T6> type6, Class<T7> type7, Class<T8> type8)
    {
        Optional<T1> comp1 = getComponent(type1);
        if (comp1.isEmpty()) return Optional.empty();

        Optional<T2> comp2 = getComponent(type2);
        if (comp2.isEmpty()) return Optional.empty();

        Optional<T3> comp3 = getComponent(type3);
        if (comp3.isEmpty()) return Optional.empty();

        Optional<T4> comp4 = getComponent(type4);
        if (comp4.isEmpty()) return Optional.empty();

        Optional<T5> comp5 = getComponent(type5);
        if (comp5.isEmpty()) return Optional.empty();

        Optional<T6> comp6 = getComponent(type6);
        if (comp6.isEmpty()) return Optional.empty();

        Optional<T7> comp7 = getComponent(type7);
        if (comp7.isEmpty()) return Optional.empty();

        Optional<T8> comp8 = getComponent(type8);
        return comp8.map(t8 -> new Results.Result8<>(comp1.get(), comp2.get(), comp3.get(), comp4.get(), comp5.get(), comp6.get(), comp7.get(), t8));
    }
}
