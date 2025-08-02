local BUTTON_LEFT_PADDING = 5
local BUTTON_OFFSET = 1
local BUTTON_HEIGHT = 13
local BUTTON_CLICKBOX_HEIGHT = 11
local RADIO_BUTTON_GROUP = "tab_buttons"

local function transformString(str)
    -- Replace spaces with underscores
    str = string.gsub(str, " ", "_")
    -- Remove all non-alphabetic and non-underscore characters
    str = string.gsub(str, "[^a-zA-Z_]", "")
    -- Convert to lowercase
    str = string.lower(str)
    return str
end

local function createButton(lastButton, label, view, selected)

    local position;
    if lastButton == nil then
        position = {BUTTON_LEFT_PADDING, 0}
    else
        position = {
            type = "relative",
            parent = lastButton.name,
            offset = {BUTTON_OFFSET, 0},
            is_right = true
        }
    end

    local button =
    {
        widget = "mock_chat:tab_button",
        name = transformString(label),
        position = position,
        bounds = {0, BUTTON_HEIGHT},
        clickbox_size = {0, BUTTON_CLICKBOX_HEIGHT},
        button = {
            label = label,
            on_press = {
                script = "mock_chat:widget/tab_view/button_change_content",
                input = view
            }
        },
        radio_group = {
            group = RADIO_BUTTON_GROUP,
            selected = selected,
            -- hijack label 'cause I'm lazy
            label = view
        }
    }

    return button
end

function init(input)
    --print(core.dump(input))

    local childs = {}

    for i = 0, #input.labels, 1 do
        childs[i + 1] = createButton(childs[i], input.labels[i], input.views[i], i == input.default)
    end

    return {
        tables = {
            ["mock_chat:tab_view"] = input
        },
        children = childs
    }
end
