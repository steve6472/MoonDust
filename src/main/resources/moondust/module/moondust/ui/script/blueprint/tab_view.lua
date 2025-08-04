local sides = {
    top = {
        padding = {4, 0},
        offset = {1, 0},
        height = 13,
        clickboxHeight = 11,
    },
    left = {
        padding = {0, 4},
        offset = {0, 14},
        height = 13,
        clickboxHeight = 11,
    }
}

local RADIO_BUTTON_GROUP = "tab_buttons"

local function transformString(str)
    -- Replace spaces with underscores
    str = string.gsub(str, " ", "_")
    -- Remove all non-alphabetic and non-underscore characters
    str = string.gsub(str, "[^a-zA-Z_]", ".")
    -- Convert to lowercase
    str = string.lower(str)
    return str
end

local function createButton(lastButton, label, view, selected, buttonType)

    local position;
    if lastButton == nil then
        position = sides[buttonType].padding
    else
        position = {
            type = "relative",
            parent = lastButton.name,
            offset = {sides[buttonType].offset[1], sides[buttonType].offset[2]},
            is_right = buttonType == "top" or buttonType == "bottom"
        }
    end

    local button =
    {
        widget = "moondust:sub/tab_button/"..buttonType,
        name = "tab_button_"..transformString(label),
        position = position,
        bounds = {0, sides[buttonType].height},
        clickbox_size = {0, sides[buttonType].clickboxHeight},
        button = {
            label = label,
            on_press = {
                script = "moondust:widget/tab_view/button_change_content",
                input = "view_content_"..transformString(view)
            }
        },
        render_order = {
            widget = "outer_frame",
            order = "below"
        },
        radio_group = {
            group = RADIO_BUTTON_GROUP,
            selected = selected,
            -- hijack label 'cause I'm lazy
            label = "view_content_"..transformString(view)
        }
    }

    return button
end

local function createContent(widget)

    local child =
    {
        widget = widget,
        name = "view_content_"..transformString(widget),
        position = {0, 0},
        bounds = {"100%", "100%"}
    }

    return child
end

function init(input)
    --print(core.dump(input))

    local tabs = {}
    local contents = {}

    for i = 0, #input.labels, 1 do
        tabs[i + 1] = createButton(tabs[i], input.labels[i], input.views[i], i == input.default, input.side)
        contents[i + 1] = createContent(input.views[i])
    end

    local children =
    {
        {
            widget = "moondust:empty",
            name = "content",
            position = {0, 0},
            children = contents
        }
    }

    for _, v in pairs(tabs) do
        table.insert(children, v)
    end

    --print(core.dump(children))

    return {
        tables = {
            ["moondust:tab_view"] = input
        },
        children = children
    }
end
