--#include moondust:widget/event/global_mouse_button

local scrollUp = "up"
local scrollDown = "down"
local scrollDirectionKey = "moondust:mouse/last_scroll_direction";
local scrollDisplayMs = 100
local scrollDisplayKey = "moondust:mouse/last_scroll"

local function tick(widget)
    local data = widget:customData()
    local currentTime = core.nanoTime()
    local lastScroll = data:getNumber(scrollDisplayKey)

    if lastScroll == 0 or currentTime - lastScroll <= scrollDisplayMs * 1000000 then
        return
    end

    data:removeNumber(scrollDisplayKey)
    if data:getString(scrollDirectionKey) == scrollUp then
        widget:getChild("scroll_up"):setVisible(false)
    elseif data:getString(scrollDirectionKey) == scrollDown then
        widget:getChild("scroll_down"):setVisible(false)
    end
end

local function scroll(widget, yOffset)
    local data = widget:customData()
    local currentTime = core.nanoTime()
    data:setNumber(scrollDisplayKey, currentTime)

    if yOffset < 0 then
        widget:getChild("scroll_up"):setVisible(false)
        widget:getChild("scroll_down"):setVisible(true)
        data:setString(scrollDirectionKey, scrollDown)
    else
        widget:getChild("scroll_down"):setVisible(false)
        widget:getChild("scroll_up"):setVisible(true)
        data:setString(scrollDirectionKey, scrollUp)
    end
end

local function mouse(widget, mouseData)
    local isPress = mouseData.action == globalMouseButton.action.press

    if mouseData.button == globalMouseButton.button.left then
        widget:getChild("lmb"):setVisible(isPress)
    elseif mouseData.button == globalMouseButton.button.right then
        widget:getChild("rmb"):setVisible(isPress)
    elseif mouseData.button == globalMouseButton.button.middle then
        widget:getChild("mmb"):setVisible(isPress)
    end
end

events.onRender:register(tick)
events.onGlobalScroll:register(scroll)
events.onGlobalMouseButton:register(mouse)
