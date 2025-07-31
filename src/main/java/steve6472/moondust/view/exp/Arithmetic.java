package steve6472.moondust.view.exp;

import steve6472.moondust.view.property.Property;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
@SuppressWarnings("unused")
public interface Arithmetic
{
    @FunctionalInterface
    interface Int extends Exp<Integer>
    {
        static Int copy(Property<Integer> prop) { return prop::get; }
        static Int sub(Property<Integer> left, Property<Integer> right) { return () -> left.get() - right.get(); }
        static Int add(Property<Integer> left, Property<Integer> right) { return () -> left.get() + right.get(); }
        static Int mul(Property<Integer> left, Property<Integer> right) { return () -> left.get() * right.get(); }
        static Int div(Property<Integer> left, Property<Integer> right) { return () -> left.get() / right.get(); }
        static Int shl(Property<Integer> left, Property<Integer> right) { return () -> left.get() << right.get(); }
        static Int shr(Property<Integer> left, Property<Integer> right) { return () -> left.get() >> right.get(); }
    }

    @FunctionalInterface
    interface Number extends Exp<Double>
    {
        static Number copy(Property<Double> prop) { return prop::get; }
        static Number sub(Property<Double> left, Property<Double> right) { return () -> left.get() - right.get(); }
        static Number add(Property<Double> left, Property<Double> right) { return () -> left.get() + right.get(); }
        static Number mul(Property<Double> left, Property<Double> right) { return () -> left.get() * right.get(); }
        static Number div(Property<Double> left, Property<Double> right) { return () -> left.get() / right.get(); }
    }

    static Int numToInt(Property<Double> number) { return () -> number.get().intValue(); }
    static Number intToNum(Property<Integer> number) { return () -> number.get().doubleValue(); }
}
