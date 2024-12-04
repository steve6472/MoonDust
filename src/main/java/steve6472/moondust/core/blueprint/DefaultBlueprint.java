package steve6472.moondust.core.blueprint;

/**
 * Created by steve6472
 * Date: 12/2/2024
 * Project: MoonDust <br>
 */
public record DefaultBlueprint<T extends Blueprint>(BlueprintEntry<T> entry, T defaultValue)
{
}
