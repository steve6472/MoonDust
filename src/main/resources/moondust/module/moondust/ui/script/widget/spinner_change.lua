--#include moondust:widget/keys/spinner

local function change(widget, event)
    local parent = widget:getParent()
    local parentData = parent:customData()
    local increment = parentData:getNumber(spinner.increment)
    local value = parentData:getNumber(spinner.value)

    if widget:getName() == "up" then
        parentData:setNumber(spinner.value, value + increment)
    else
        parentData:setNumber(spinner.value, value - increment)
    end
end

events.onMouseRelease:register(change)
