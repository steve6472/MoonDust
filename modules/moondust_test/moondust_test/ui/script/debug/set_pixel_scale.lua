--#include moondust:widget/util

local function press(widget)
    if not buttonTest(widget) then return end
    local data = widget:customData()
    local scale = data:getNumber("moondust:debug/set_pixel_scale")
    if scale == nil then return end
    MoonDust.setPixelScale(scale)
end

events.onMouseRelease:register(press)
