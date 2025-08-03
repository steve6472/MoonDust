--#include moondust:widget/keys/generic
--#include moondust:widget/util

local function propertyChange(widget, changed)
    if (changed.property ~= "enabled") then return end

    if (changed.new_value) then
        changeCurrentSprite(widget, ids.sprite.normal)
    else
        changeCurrentSprite(widget, ids.sprite.disabled)
    end
    MoonDust.replaceStyleText(widget, pickStyle(widget, "ignore"))
end

events.onPropertyChange:register(propertyChange)

local function charInput(widget, codepoint)

end

local function keyInput(widget, keyInput)

end

events.onCharInput:register(charInput)
events.onKeyInput:register(keyInput)
