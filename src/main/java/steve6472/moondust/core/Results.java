package steve6472.moondust.core;

/**
 * Created by steve6472
 * Date: 12/16/2024
 * Project: MoonDust <br>
 */
public interface Results
{
    record Result1<T1>(T1 comp1) {}
    record Result2<T1, T2>(T1 comp1, T2 comp2) {}
    record Result3<T1, T2, T3>(T1 comp1, T2 comp2, T3 comp3) {}
    record Result4<T1, T2, T3, T4>(T1 comp1, T2 comp2, T3 comp3, T4 comp4) {}
    record Result5<T1, T2, T3, T4, T5>(T1 comp1, T2 comp2, T3 comp3, T4 comp4, T5 comp5) {}
    record Result6<T1, T2, T3, T4, T5, T6>(T1 comp1, T2 comp2, T3 comp3, T4 comp4, T5 comp5, T6 comp6) {}
    record Result7<T1, T2, T3, T4, T5, T6, T7>(T1 comp1, T2 comp2, T3 comp3, T4 comp4, T5 comp5, T6 comp6, T7 comp7) {}
    record Result8<T1, T2, T3, T4, T5, T6, T7, T8>(T1 comp1, T2 comp2, T3 comp3, T4 comp4, T5 comp5, T6 comp6, T7 comp7, T8 comp8) {}
}
