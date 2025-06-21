--#include moondust:widget/keys/generic
--#include moondust:widget/util

local function mouseEnter(widget)
    if widget:isEnabled() and not widget:internalStates().hovered then
        changeCurrentSprite(widget, ids.sprite.hover)
    end
end

local function mouseLeave(widget)
    if widget:isEnabled() and widget:internalStates().hovered then
        changeCurrentSprite(widget, ids.sprite.normal)
    end
end

local function mousePress(widget)
    if widget:isEnabled() then
        changeCurrentSprite(widget, ids.sprite.pressed)
    end
end

local function mouseRelease(widget)
    if widget:isEnabled() then
        if widget:internalStates().hovered then
            changeCurrentSprite(widget, ids.sprite.hover)
        else
            changeCurrentSprite(widget, ids.sprite.normal)
        end
    end
end

local function enableChange(widget)
    if widget:isEnabled() then
        if widget:internalStates().hovered then
            changeCurrentSprite(widget, ids.sprite.hover)
        else
            changeCurrentSprite(widget, ids.sprite.normal)
        end
    else
        changeCurrentSprite(widget, ids.sprite.disabled)
    end
end

events.onMouseEnter:register(mouseEnter)
events.onMouseLeave:register(mouseLeave)
events.onMousePress:register(mousePress)
events.onMouseRelease:register(mouseRelease)
events.onEnableChange:register(enableChange)