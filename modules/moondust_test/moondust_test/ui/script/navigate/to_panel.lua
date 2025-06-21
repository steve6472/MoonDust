--#include moondust:widget/util

local function action(widget)
    if not buttonTest(widget) then return end

    if inputArgs == nil then
        warning("Missing inputs, required: to_open, to_close. Aborting navigation.")
        return
    end

    MoonDust.removePanel(inputArgs.to_close)
    local opened = MoonDust.addPanel(inputArgs.to_open)
    if opened == nil then return end

    local navTable = opened:getTable("moondust:navigation")
    if navTable == nil then navTable = {history={}} end

    navTable.history[#navTable.history + 1] = inputArgs.to_close
    navTable.current = inputArgs.to_open
    --print(core.dump(navTable))
    opened:setTable("moondust:navigation", navTable)
end

events.onMouseRelease:register(action)