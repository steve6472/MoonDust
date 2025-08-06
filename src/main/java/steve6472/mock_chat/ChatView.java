package steve6472.mock_chat;

import steve6472.core.registry.Key;
import steve6472.core.util.RandomUtil;
import steve6472.moondust.view.PanelView;
import steve6472.moondust.view.property.BooleanProperty;
import steve6472.moondust.view.property.TableProperty;
import steve6472.radiant.LuauTable;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

/**
 * Created by steve6472
 * Date: 8/3/2025
 * Project: MoonDust <br>
 */
public class ChatView extends PanelView
{
    /* Notes
     *
     * Going further, before view init, all widgets should be already created.
     * No widgets that are meant to be controlled from view should be added/removed, only visibility change can happen.
     *
     * (For example, a chat message entry can be added/removed as it may have only buttons that execute commands that may modify only the parent widgets properties)
     */

    public static final String NAMESPACE = "mock_chat";

    public ChatView(Key key) { super(key); }

    private TableProperty chatEntires;

    @Override
    protected void createProperties()
    {
        BooleanProperty escapeChatEnable = findProperty("escape_chat_enable:checked");
        BooleanProperty navigateBackEnabled = findProperty("navigate_back:enabled");
        chatEntires = findProperty("chat:entries");
//        chatEntires.addListener((_, o, n) -> {System.out.println(o + " -> " + n);});

        // Properties can be set from global values if needed here
//        escapeChatEnable.set(true);

        navigateBackEnabled.bind(escapeChatEnable.copyFrom());
    }

    private LuauTable createMessageEntry(String iconPath, String username, String messageBody)
    {
        LuauTable table = new LuauTable();
        table.add("icon_path", iconPath);
        table.add("username", username);
        table.add("message_body", messageBody);
        return table;
    }

    @Override
    protected void createCommandListeners()
    {
        addCommandListener(key("send_message"), _ -> {
            LuauTable luauTable = chatEntires.get();
            int name = RandomUtil.randomInt(0, names.size() - 1);
            luauTable.add(luauTable.table().size() + 1, createMessageEntry(
                profilePictures.get(name),
                names.get(name),
                messages.get(RandomUtil.randomInt(0, messages.size() - 1))));
            chatEntires.set(luauTable);
        });

        // Debug
        addCommandListener(key("reset_chat"), _ -> chatEntires.set(new LuauTable()));

        // Debug
        addCommandListener(key("copy_panel"), _ -> {
            // TODO: use
//            inputText.get();
//            inputText.set("");
            String myString = panel.toString();
            myString = myString.replaceAll("\\R", "\\\\n");
            StringSelection stringSelection = new StringSelection(myString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });
    }

    private Key key(String id)
    {
        return Key.withNamespace(NAMESPACE, id);
    }

    final List<String> messages = List.of(
        "I am Groot.",
        "Why so serious?",
        "Yippee-ki-yay!",
        "To the moon!",
        "I'll be back.",
        "Say hello!",
        "Bazinga!",
        "Winter is here.",
        "Wakanda forever.",
        "42.",
        "I'm Batman.",
        "Expecto Patronum!",
        "Red pill?",
        "Run, Forrest!",
        "So it goes.",
        "Hodor."
    );

    List<String> names = List.of(
        "Mario",
        "Yoda",
        "Frodo",
        "Elsa",
        "Neo",
        "Sherlock",
        "Thanos",
        "Katniss"
    );

    List<String> profilePictures = List.of(
        "mock_chat:users/mario",
        "mock_chat:users/yoda",
        "mock_chat:users/frodo",
        "mock_chat:users/elsa",
        "mock_chat:users/neo",
        "mock_chat:users/sherlock",
        "mock_chat:users/thanos",
        "mock_chat:users/katniss"
    );
}
