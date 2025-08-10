--#include moondust:widget/keys/generic
--#include moondust:widget/util

local GLFW_RELEASE = 0
local GLFW_KEY_BACKSPACE = 259

local function propertyChange(widget, changed)
    if (changed.property ~= "enabled") then return end

    if (changed.new_value) then
        changeCurrentSprite(widget, ids.sprite.normal)
    else
        changeCurrentSprite(widget, ids.sprite.disabled)
    end
    MoonDust.replaceStyleText(widget, pickStyle(widget, "ignore"))
end

local function charInput(widget, codepoint)
    if (not widget:isEnabled() or not widget:isVisible()) then return end

    local text = widget:getComponent("moondust:text")
    if (text == nil) then return end

    text.text.parts[1].text = text.text.parts[1].text .. utf8.char(codepoint)
    widget:addComponent("moondust:text", text)
end

local function keyInputFun(widget, keyInput)
    if (not widget:isEnabled() or not widget:isVisible()) then return end
    if (keyInput.action == GLFW_RELEASE) then return end

    local text = widget:getComponent("moondust:text")
    if (text == nil) then return end

    local textStr = text.text.parts[1].text
    if (keyInput.key == GLFW_KEY_BACKSPACE) then
        if (textStr ~= nil and textStr ~= '') then
            textStr = string.substring(textStr, 0, string.length(textStr) - 1)
        end
    end
    text.text.parts[1].text = textStr
    widget:addComponent("moondust:text", text)
end

events.onCharInput:register(charInput)
events.onKeyInput:register(keyInputFun)
events.onPropertyChange:register(propertyChange)
