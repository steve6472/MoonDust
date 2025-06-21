--#include moondust:widget/util

local changeLog = {}
local interval = 1e9 * 9  -- 9 seconds in nanoseconds

local function action(widget)
    local path = widget:getPath()
    local currentTime = core.nanoTime()
    local lastTime = changeLog[path] or 0

    if (currentTime - lastTime) >= interval then
        changeLog[path] = currentTime
        widget:runJavaFunc("moondust:button/random_toggle_enabled")
    end
end

events.onRender:register(action)