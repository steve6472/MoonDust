--#include moondust:widget/util

local function action(widget)
    if not buttonTest(widget) then return end

    widget:sendCommand(inputArgs.key, inputArgs.input)
end

events.onMouseRelease:register(action)