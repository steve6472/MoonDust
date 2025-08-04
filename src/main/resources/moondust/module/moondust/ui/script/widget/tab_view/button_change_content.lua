--#include moondust:widget/util

local function action(widget)
    if not buttonTest(widget) then return end

    -- inputArgs are a key to a widget
    if inputArgs == nil then
        warning("Missing inputs, required string")
        return
    end

    local content = widget:getParent():getChild("content")

    for _, v in ipairs(content:getChildrenNames()) do
        local child = content:getChild(v)
        local visible = false;
        if (child:getName() == inputArgs) then
            visible = true
        end

        child:setVisible(visible)
    end
end

events.onMouseRelease:register(action)