--#include moondust:widget/keys/checkbox
--#include moondust:widget/util

local function init(widget)
    local data = widget:customData()
    local property = data:getString("moondust:debug/text_lines")
    if property == nil then return end
    local val = MoonDustDebug.getTextLines(property)
    widget:changeProperty("checked", val)
end

local function propertyChange(widget, changed)
    if (changed.property == "checked") then
        local data = widget:customData()
        local property = data:getString("moondust:debug/text_lines")
        if property == nil then return end
        MoonDustDebug.setTextLines(property, changed.new_value)
    end
end

events.onInit:register(init)
events.onPropertyChange:register(propertyChange)
