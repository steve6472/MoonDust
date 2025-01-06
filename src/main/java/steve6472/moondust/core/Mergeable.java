package steve6472.moondust.core;

/**
 * Created by steve6472
 * Date: 1/2/2025
 * Project: MoonDust <br>
 */
public interface Mergeable<T>
{
    T merge(T left, T right);
}
