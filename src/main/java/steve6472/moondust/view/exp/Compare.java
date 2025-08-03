package steve6472.moondust.view.exp;

import steve6472.moondust.view.property.Property;

import java.util.Objects;

/**
 * Created by steve6472
 * Date: 7/23/2025
 * Project: MoonDust <br>
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Compare extends Exp<Boolean>
{
    interface Int
    {
        static Compare greaterThan(Property<Integer> left, Property<Integer> right) { return () -> left.get() > right.get(); }
        static Compare lessThan(Property<Integer> left, Property<Integer> right) { return () -> left.get() < right.get(); }
        static Compare greaterThanOrEqual(Property<Integer> left, Property<Integer> right) { return () -> left.get() >= right.get(); }
        static Compare lessThanOrEqual(Property<Integer> left, Property<Integer> right) { return () -> left.get() <= right.get(); }
        static Compare equal(Property<Integer> left, Property<Integer> right) { return () -> Objects.equals(left.get(), right.get()); }
        static Compare notEqual(Property<Integer> left, Property<Integer> right) { return () -> !Objects.equals(left.get(), right.get()); }
    }

    interface Number
    {
        static Compare greaterThan(Property<Double> left, Property<Double> right) { return () -> left.get() > right.get(); }
        static Compare lessThan(Property<Double> left, Property<Double> right) { return () -> left.get() < right.get(); }
        static Compare greaterThanOrEqual(Property<Double> left, Property<Double> right) { return () -> left.get() >= right.get(); }
        static Compare lessThanOrEqual(Property<Double> left, Property<Double> right) { return () -> left.get() <= right.get(); }
        static Compare equal(Property<Double> left, Property<Double> right) { return () -> Objects.equals(left.get(), right.get()); }
        static Compare notEqual(Property<Double> left, Property<Double> right) { return () -> !Objects.equals(left.get(), right.get()); }
    }

    interface Bool
    {
        static Compare equal(Property<Boolean> left, Property<Boolean> right) { return () -> Objects.equals(left.get(), right.get()); }
        static Compare notEqual(Property<Boolean> left, Property<Boolean> right) { return () -> !Objects.equals(left.get(), right.get()); }
        static Compare from(Property<Boolean> property) { return property::get; }
    }
}
