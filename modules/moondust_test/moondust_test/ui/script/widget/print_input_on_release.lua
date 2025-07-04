--#include moondust:widget/util

local function action(widget)
    if not buttonTest(widget) then return end

    if inputArgs == nil then
        info("Input: nil")
    else
        info("Input: "..core.dump(inputArgs))
    end
end

events.onMouseRelease:register(action)