--#include moondust:widget/keys/generic
--#include moondust:widget/util

local PADDING = 8

local function updateText(widget)
    local label = widget:getTable("moondust:button")
    if label == nil then return end
    label = label.label

    local text = widget:getComponent("moondust:text");
    text.text.parts[1].text = label
    local maxWidth = MoonDust.getTextMaxWidth(text)
    maxWidth = math.floor(maxWidth)

    local bounds = widget:getComponent("moondust:bounds")
    bounds.width = maxWidth + PADDING

    text.text.max_width = bounds.width
    if maxWidth % 2 == 1 then
        text.text.max_width = text.text.max_width - 1
    end

    local clickboxSize = widget:getComponent("moondust:clickbox_size")
    clickboxSize.width = maxWidth + PADDING

    text.text.max_height = MoonDust.getTextTotalCharHeight(text)

    widget:addComponent("moondust:text", text)
    widget:addComponent("moondust:bounds", bounds)
    widget:addComponent("moondust:clickbox_size", clickboxSize)
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

events.onInit:register(init)
events.onDataChanged:register(dataChanged)
