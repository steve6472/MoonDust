--#include moondust:widget/keys/checkbox
--#include moondust:widget/keys/generic
--#include moondust:widget/util

local function updateCheckedSprite(widget)
    local checked = widget:customData():getFlag(checkbox.checked)
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
    updateCheckedSprite(widget)
end

local function enableChange(widget)
    updateCheckedSprite(widget)
    MoonDust.replaceStyleText(widget, pickStyle(widget, false))
end

local function dataChanged(widget, changed)
    if changed.type == "flag" and changed.key == checkbox.checked then
        updateCheckedSprite(widget)
    end
end

local function toggle(widget)
    if not widget:isEnabled() then return end
    if not buttonTest(widget) then return end
    local data = widget:customData()
    local checked = data:getFlag(checkbox.checked)
    data:setFlag(checkbox.checked, not checked)
end

events.onInit:register(init)
events.onDataChanged:register(dataChanged)
events.onEnableChange:register(enableChange)
events.onMouseRelease:register(toggle)
