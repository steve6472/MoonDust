--#include moondust:widget/util

local function action(widget)
    if not buttonTest(widget) then return end

    -- inputArgs are a key to a widget
    if inputArgs == nil then
        warning("Missing inputs, required string")
        return
    end

    local child =
    {
        widget = inputArgs,
        name = "view_content",
        position = {0, 0},
        bounds = {"100%", "100%"}
    }

    local content = widget:getParent():getChild("content")

    for k, v in ipairs(content:getChildrenNames()) do
        content:removeChild(v)
    end

    content:addChildren({child})
end

events.onMouseRelease:register(action)