package steve6472.moondust;

import org.lwjgl.system.MemoryStack;
import steve6472.core.registry.Key;
import steve6472.flare.Camera;
import steve6472.flare.core.FlareApp;
import steve6472.flare.core.FrameInfo;
import steve6472.flare.input.KeybindUpdater;
import steve6472.flare.registry.FlareRegistries;
import steve6472.flare.render.UIFontRender;
import steve6472.flare.render.UILineRender;
import steve6472.flare.render.UIRenderSystem;
import steve6472.flare.ui.font.style.FontStyle;
import steve6472.moondust.builtin.BuiltinEventCalls;
import steve6472.moondust.widget.Panel;
import steve6472.test.DebugUILines;

/**
 * Created by steve6472
 * Date: 12/1/2024
 * Project: MoonDust <br>
 */
public class MoonDustTest extends FlareApp
{
    public MoonDustTest()
    {
    }

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
        initRegistry(MoonDustRegistries.POSITION_BLUEPRINT_TYPE);
        BuiltinEventCalls.init();
        TestEventCalls.init(this);
    }

    @Override
    public void loadSettings()
    {
    }

    @Override
    protected void createRenderSystems()
    {
        addRenderSystem(new UIRenderSystem(masterRenderer(), new MoonDustUIRender(this), 256f));
        addRenderSystem(new UIFontRender(masterRenderer(), new MoonDustUIFontRender()));
        addRenderSystem(new UILineRender(masterRenderer(), new DebugWidgetUILines()));
        addRenderSystem(new UILineRender(masterRenderer(), new DebugUILines()));

        new MoonDustCallbacks().init(window().callbacks(), input());
    }

    @Override
    public void postInit()
    {
        Panel testPanel = Panel.create(Key.withNamespace("moondust_test", "panel"));
        testPanel.clearFocus();
        MoonDust.getInstance().addPanel(testPanel);

        KeybindUpdater.updateKeybinds(MoonDustRegistries.KEYBIND, input());
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
