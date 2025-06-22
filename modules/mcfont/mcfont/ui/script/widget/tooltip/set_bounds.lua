local widthPadding = 8;

local function init(widget)

    local title = widget:getChild("title")
    local titleText = title:getComponent("moondust:text")
    local titleTextWidth = MoonDust.getTextMaxWidth(titleText)

    local lore = widget:getChild("lore")
    local loreText = lore:getComponent("moondust:text")
    local loreTextWidth = MoonDust.getTextMaxWidth(loreText)

    local width = math.max(titleTextWidth, loreTextWidth)

    local bounds = {}
    bounds.width = width + widthPadding
    bounds.height = MoonDust.getTextTotalFontHeight(titleText) + 4
    bounds.height = bounds.height + MoonDust.getTextTotalFontHeight(loreText) - MoonDust.getTextLineCount(loreText) * 2 + 2
    widget:addComponent("moondust:bounds", bounds)
    local tooltipFrame = widget:getChild("tooltip/frame")
    tooltipFrame:addComponent("moondust:bounds", bounds)
    tooltipFrame:addComponent("moondust:sprite_size", bounds)

    local tooltipBackground = widget:getChild("tooltip/background")
    tooltipBackground:addComponent("moondust:bounds", bounds)
    tooltipBackground:addComponent("moondust:sprite_size", bounds)
end

events.onInit:register(init)