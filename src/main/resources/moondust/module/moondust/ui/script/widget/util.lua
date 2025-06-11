--#noscript

local function changeCurrentSprite(widget, sprite)
    if widget == nil or sprite == nil then return end
    local currentSprite = widget:getComponent("moondust:current_sprite")
    if currentSprite == nil then return end
    widget:addComponent("moondust:current_sprite", sprite)
end

local function pickStyle(widget, hoverOverride)
    if not (hoverOverride == true) and not (hoverOverride == false) and not (hoverOverride == "ignore") and not (hoverOverride == nil) then
        error("hoverOverride argument has invalid value")
    end

    if hoverOverride == nil then
        hoverOverride = "ignore"
    end

    local styles = widget:getComponent("moondust:styles")
    if styles == nil then return nil end

    local hover;
    if overOverride == "ignore" then
        hover = widget:internalStates().hovered
    else
        hover = hoverOverride == true
    end

    if widget:isEnabled() then
        if hover then
            return styles[ids.style.hover]
        else
            return styles[ids.style.normal]
        end
    else
        return styles[ids.style.disabled]
    end
end

local function orElse(value, elseValue)
    if value == nil then
        return elseValue
    else
        return value
    end
end

local function buttonTest(widget)
    return widget:isEnabled() and widget:isVisible() and widget:isClickable() and widget:internalStates().directHover
end