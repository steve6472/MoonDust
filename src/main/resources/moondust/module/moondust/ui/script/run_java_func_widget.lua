--#include moondust:widget/util

local function action(widget)
    if not buttonTest(widget) then return end

    widget:runJavaFunc(inputArgs);
end

events.onMouseRelease:register(action)