package steve6472.mock_chat;

import steve6472.core.registry.Key;
import steve6472.core.util.RandomUtil;
import steve6472.flare.settings.VisualSettings;
import steve6472.moondust.MoonDustSettings;
import steve6472.moondust.view.PanelView;
import steve6472.moondust.view.property.BooleanProperty;
import steve6472.moondust.view.property.Property;
import steve6472.moondust.view.property.StringProperty;
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
    private StringProperty textFieldText;
    private StringProperty username;
    private StringProperty pfp;

    @Override
    protected void createProperties()
    {
        BooleanProperty escapeChatEnable = findProperty("escape_chat_enable:checked");
        BooleanProperty navigateBackEnabled = findProperty("navigate_back:enabled");
        BooleanProperty sendEnable = findProperty("send_button:enabled");
        chatEntires = findProperty("chat:entries");
        textFieldText = findProperty("text_input:text");
        username = findProperty("username:text");
        pfp = findProperty("pfp:text");

        // Quick debug
        // debugChange(textFieldText);

        // Bind setting to a property
        bindSetting(MoonDustSettings.DEBUG_CURSOR, findProperty("enable_debug_cursor:checked"));

        // Properties can be set from global values if needed here
        escapeChatEnable.set(true);

        // Navigate back button will be enabled based on value from checkbox
        navigateBackEnabled.bind(escapeChatEnable.copyFrom());

        sendEnable.bind(() -> !textFieldText.get().isBlank(), textFieldText);
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
        addCommandListener(key("add_fake_message"), _ -> {
            LuauTable luauTable = chatEntires.get();
            int name = RandomUtil.randomInt(0, names.size() - 1);
            luauTable.add(luauTable.table().size() + 1, createMessageEntry(
                profilePictures.get(name),
                names.get(name),
                messages.get(RandomUtil.randomInt(0, messages.size() - 1))));
            chatEntires.set(luauTable);
        });

        addCommandListener(key("send_message"), _ -> {
            LuauTable luauTable = chatEntires.get();

            luauTable.add(luauTable.table().size() + 1, createMessageEntry(
                pfp.get(),
                username.get(),
                textFieldText.get()));
            chatEntires.set(luauTable);
            textFieldText.set("");
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

    private void debugChange(Property<?> property)
    {
        property.addListener((_, o, n) -> System.out.println(o + " -> " + n));
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
        "no_git:users/mario",
        "no_git:users/yoda",
        "no_git:users/frodo",
        "no_git:users/elsa",
        "no_git:users/neo",
        "no_git:users/sherlock",
        "no_git:users/thanos",
        "no_git:users/katniss"
    );
}
