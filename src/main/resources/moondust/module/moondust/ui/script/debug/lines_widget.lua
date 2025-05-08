--#include moondust:widget/keys/checkbox
--#include moondust:widget/util

local function init(widget)
    local data = widget:customData()
    local property = data:getString("moondust:debug/widget_ui_lines")
    if property == nil then return end
    local val = MoonDustDebug.getWidgetUiLines(property)
    data:setFlag(checkbox.checked, val)
end

local function press(widget, changed)
    if not changed.key == checkbox.checked then return end
    local data = widget:customData()
    local property = data:getString("moondust:debug/widget_ui_lines")
    if property == nil then return end
    MoonDustDebug.setWidgetUiLines(property, changed.new)
end

events.onInit:register(init)
events.onDataChanged:register(press)
