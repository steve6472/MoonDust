package steve6472.mock_chat;

import steve6472.core.registry.Key;
import steve6472.moondust.view.PanelView;
import steve6472.moondust.view.exp.Compare;
import steve6472.moondust.view.property.BooleanProperty;
import steve6472.moondust.view.property.Property;

/**
 * Created by steve6472
 * Date: 8/3/2025
 * Project: MoonDust <br>
 */
public class ChatView extends PanelView
{
    /* Notes
     *
     * Widget addChild, removeChild have view update functions, possibly remove them
     * Change tab_view to change widget visibility instead of removing/adding children
     *
     * Going further, before view init, all widgets should be already created.
     * No widgets that are meant to be controlled from view should be added/removed, only visibility change can happen.
     *
     * (For example, a chat message entry can be added/removed as it may have only buttons that execute commands that may modify only the parent widgets properties)
     */

    public static final String NAMESPACE = "mock_chat";

    public ChatView(Key key) { super(key); }

    @Override
    protected void createProperties()
    {
        BooleanProperty escapeChatEnable = findProperty("escape_chat_enable:checked");
        BooleanProperty navigateBackEnabled = findProperty("navigate_back:enabled");

        // Properties can be set from global values if needed here
//        escapeChatEnable.set(true);

        navigateBackEnabled.bind(Compare.Bool.from(escapeChatEnable), escapeChatEnable);
    }

    @Override
    protected void createCommandListeners()
    {
        addCommandListener(key("send_message"), input -> {
            // TODO: use
//            inputText.get();
//            inputText.set("");
        });
    }

    private Key key(String id)
    {
        return Key.withNamespace(NAMESPACE, id);
    }
}
