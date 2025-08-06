local function createChatEntry(entryIndex, chatEntryData)

    local position

    if (entryIndex == 1) then
        position = {0, 0}
    else
        position = {
            type = "relative",
            parent = "entry_"..(entryIndex - 1),
            offset = {0, 20}
        }
    end

    return
    {
        widget = "mock_chat:chat_entry",
        name = "entry_"..entryIndex,
        position = position,
        bounds = {"100%", 20},
        ["mock_chat:chat_entry"] = chatEntryData
    }
end

local function propertyChange(widget, changed)
    --print(core.dump(changed))
    if (changed.property ~= "entries") then return end

    for _, v in ipairs(widget:getChildrenNames()) do
        widget:removeChild(v)
    end

    local toAdd = {}
    for k, v in ipairs(changed.new_value) do
        toAdd[k] = createChatEntry(k, v)
    end
    widget:addChildren(toAdd)
end

events.onPropertyChange:register(propertyChange)