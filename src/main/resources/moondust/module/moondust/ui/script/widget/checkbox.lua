--#include moondust:widget/keys/checkbox
--#include moondust:widget/keys/generic
--#include moondust:widget/util

local function updateCheckedSprite(widget, checked)
    if checked then
        if widget:isEnabled() then
            changeCurrentSprite(widget, ids.sprite.checked)
        else
            changeCurrentSprite(widget, ids.sprite.checkedDisabled)
        end
    else
        if widget:isEnabled() then
            changeCurrentSprite(widget, ids.sprite.unchecked)
        else
            changeCurrentSprite(widget, ids.sprite.uncheckedDisabled)
        end
    end
end

local function init(widget)
    updateCheckedSprite(widget, widget:getPropertyValue("checked"))
end

local function toggle(widget)
    if not buttonTest(widget) then return end

    widget:changeProperty("checked", not widget:getPropertyValue("checked"))
end

events.onInit:register(init)
events.onMouseRelease:register(toggle)

local function propertyChange(widget, changed)
    --print(core.dump(changed))
    if (changed.property == "checked") then
        updateCheckedSprite(widget, changed.new_value)

        --MoonDust.replaceStyleText(widget, pickStyle(widget, false))
    elseif (changed.property == "enabled") then
        updateCheckedSprite(widget, widget:getPropertyValue("checked"))
    end
end

events.onPropertyChange:register(propertyChange)
