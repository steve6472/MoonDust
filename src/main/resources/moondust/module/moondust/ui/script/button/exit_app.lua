--#include moondust:widget/util

local function action(widget)
    if not buttonTest(widget) then return end

    MoonDust.runJavaFunc("moondust:button/exit_app")
end

events.onMouseRelease:register(action)