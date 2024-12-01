package steve6472.moondust;

import org.lwjgl.system.MemoryStack;
import steve6472.flare.Camera;
import steve6472.flare.core.FlareApp;
import steve6472.flare.core.FrameInfo;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDustTest extends FlareApp
{
    @Override
    protected void preInit()
    {

    }

    @Override
    protected Camera setupCamera()
    {
        return new Camera();
    }

    @Override
    protected void initRegistries()
    {

    }

    @Override
    public void loadSettings()
    {

    }

    @Override
    protected void createRenderSystems()
    {

    }

    @Override
    public void postInit()
    {

    }

    @Override
    public void render(FrameInfo frameInfo, MemoryStack memoryStack)
    {

    }

    @Override
    public void saveSettings()
    {

    }

    @Override
    public void cleanup()
    {

    }

    @Override
    public String windowTitle()
    {
        return "MoonDust UI Test";
    }

    @Override
    public String defaultNamespace()
    {
        return "moondust";
    }
}
