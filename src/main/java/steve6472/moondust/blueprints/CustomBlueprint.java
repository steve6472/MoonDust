package steve6472.moondust.blueprints;

import steve6472.core.registry.Key;
import steve6472.core.registry.Keyable;

/**
 * Created by steve6472
 * Date: 6/9/2025
 * Project: MoonDust <br>
 */
public class CustomBlueprint implements Keyable
{
    public final BlueprintStructureFields structure;
    public final Key key;

    public CustomBlueprint(BlueprintStructureFields structure, Key key)
    {
        this.structure = structure;
        this.key = key;
    }

    @Override
    public Key key()
    {
        return key;
    }
}
