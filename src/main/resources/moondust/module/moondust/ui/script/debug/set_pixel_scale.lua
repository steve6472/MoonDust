--#include moondust:widget/util

local function press(widget)
    if not buttonTest(widget) then return end

    if inputArgs == nil then return end
    MoonDust.setPixelScale(inputArgs)
end

events.onMouseRelease:register(press)
