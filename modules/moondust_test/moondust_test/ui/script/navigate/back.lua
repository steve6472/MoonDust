--#include moondust:widget/util

local function findNavigation(widget)
    local nav = widget:getTable("moondust:navigation");
    if nav == nil then
        local parent = widget:getParent()
        if parent == nil then return nil end
        return findNavigation(parent)
    end
    return nav
end

local function action(widget)
    if not buttonTest(widget) then return end

    local navTable = findNavigation(widget)
    if navTable == nil then navTable = {history={}} end
    --print(core.dump(navTable))
    if #navTable.history == 0 then
        warning("Could not navigate back, no history. Aborting navigation.")
        return
    end

    local navigateTo = navTable.history[#navTable.history]
    table.remove(navTable.history, #navTable.history)

    MoonDust.removePanel(navTable.current)
    navTable.current = navigateTo
    local opened = MoonDust.addPanel(navigateTo)
    if opened == nil then return end
    opened:setTable("moondust:navigation", navTable)
end

events.onMouseRelease:register(action)