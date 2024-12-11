package steve6472.moondust.widget;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public interface BlueprintOverride<Target>
{
    Target override(Target source);

    Class<Target> target();
}
