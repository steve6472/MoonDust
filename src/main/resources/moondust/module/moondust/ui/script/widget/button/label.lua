--#include moondust:widget/keys/generic
--#include moondust:widget/util

local function updateText(widget)
    local label = widget:getTable("moondust:button")
    if label == nil then return end
    label = label.label

    local text = widget:getComponent("moondust:text");
    text.text.parts[1].text = label
    local maxWidth = MoonDust.getTextMaxWidth(text)
    local maxWidth = math.floor(maxWidth)
    text.text.max_width = widget:getComponent("moondust:bounds").width
    if maxWidth % 2 == 1 then
        text.text.max_width = text.text.max_width - 1
    end
    --print(widget:getPath().." "..text.text.max_width.." "..maxWidth)
    text.text.max_height = MoonDust.getTextTotalCharHeight(text)
    --print(widget:getPath().." "..text.text.max_height)
    widget:addComponent("moondust:text", text)
    -- TODO: replace style. replaceStyle(widget, pickStyle(widget, (widget.internalStates().hovered || widget.internalStates().directHover) ? Tristate.TRUE : Tristate.FALSE));
end

-- TODO: change max width when changing bounds, no clue how to detect that tho.. custom events ?
local function init(widget)
    updateText(widget)
end

local function dataChanged(widget, changed)
    if changed.key ~= "moondust:button" then return end
    if changed.previous.label == changed.new.label then return end
    updateText(widget)
end

--TODO: text style changes
--        create(key("text/hover/on"),  (Widget widget, OnMouseEnter _) -> replaceStyleText(widget, pickStyle(widget)));
--        create(key("text/hover/off"), (Widget widget, OnMouseLeave _) -> replaceStyleText(widget, pickStyle(widget)));
--        create(key("text/change_enabled"), (Widget widget, OnEnableStateChange _) -> replaceStyleText(widget, pickStyle(widget)));

events.onInit:register(init)
events.onDataChanged:register(dataChanged)
