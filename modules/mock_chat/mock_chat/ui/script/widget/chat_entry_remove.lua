--#include moondust:widget/util

local function action(widget)
    if not buttonTest(widget) then return end
    local entry = widget:getParent()

    local index = tonumber(string.sub(entry:getName(), 7, -1))

    local chat = entry:getParent()
    local entries = chat:getPropertyValue("entries")
    table.remove(entries, index)
    chat:changeProperty("entries", entries)
end

events.onMouseRelease:register(action)