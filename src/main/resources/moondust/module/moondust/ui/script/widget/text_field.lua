--#include moondust:widget/keys/generic
--#include moondust:widget/util

local GLFW_RELEASE = 0
local GLFW_KEY_BACKSPACE = 259

local function updateText(widget)
    local text = widget:getComponent("moondust:text")
    if (text == nil) then return end
    local textP = widget:getPropertyValue("text")
    if (textP == nil) then return end

    if (textP == "") then
        text.text.parts[1].text = widget:getPropertyValue("ghost_text")
        text.text.parts[1].style = widget:getComponent("moondust:styles").ghost
        widget:addComponent("moondust:text", text)
    else
        if (widget:getPropertyValue("password")) then
            text.text.parts[1].text = string.rep("*", string.length(textP))
        else
            text.text.parts[1].text = textP
        end
        if (widget:isEnabled()) then
            text.text.parts[1].style = widget:getComponent("moondust:styles").normal
        else
            text.text.parts[1].style = widget:getComponent("moondust:styles").disabled
        end
        widget:addComponent("moondust:text", text)
    end
end

local function propertyChange(widget, changed)
    if (changed.property == "enabled") then
        if (changed.new_value) then
            changeCurrentSprite(widget, ids.sprite.normal)
        else
            changeCurrentSprite(widget, ids.sprite.disabled)
        end
        MoonDust.replaceStyleText(widget, pickStyle(widget, "ignore"))
    elseif (changed.property == "text") then
        updateText(widget)
    end
end

local function charInput(widget, codepoint)
    if (not widget:isEnabled() or not widget:isVisible()) then return end

    local text = widget:getPropertyValue("text")
    text = text .. utf8.char(codepoint)
    widget:changeProperty("text", text)
end

local function keyInputFun(widget, keyInput)
    if (not widget:isEnabled() or not widget:isVisible()) then return end
    if (keyInput.action == GLFW_RELEASE) then return end

    local text = widget:getPropertyValue("text")

    if (keyInput.key == GLFW_KEY_BACKSPACE) then
        if (text ~= nil and text ~= '') then
            text = string.substring(text, 0, string.length(text) - 1)
        end
    end
    widget:changeProperty("text", text)
end

local function init(widget)
    updateText(widget)
end

events.onInit:register(init)
events.onCharInput:register(charInput)
events.onKeyInput:register(keyInputFun)
events.onPropertyChange:register(propertyChange)
