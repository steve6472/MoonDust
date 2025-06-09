--#include moondust:widget/keys/spinner
--#include moondust:widget/keys/generic
--#include moondust:widget/util

local defaultNumberFormat = "%.2f"

local function enableChange(widget)
    MoonDust.replaceStyleText(widget, pickStyle(widget, false))

    widget:getChild("up"):setEnabled(widget:isEnabled())
    widget:getChild("down"):setEnabled(widget:isEnabled())
end

local function updateLabel(widget)
    local data = widget:customData()
    local unformattedLabel = data:getString(generic.label)
    if unformattedLabel == nil then return end

    local valueNumberFormat = orElse(data:getString(spinner.numberFormat.value), defaultNumberFormat)
    local minFormat = orElse(data:getString(spinner.numberFormat.min), defaultNumberFormat)
    local maxFormat = orElse(data:getString(spinner.numberFormat.max), defaultNumberFormat)
    local incrementFormat = orElse(data:getString(spinner.numberFormat.increment), defaultNumberFormat)

    unformattedLabel = string.replace(unformattedLabel, "%value%", string.format(valueNumberFormat, data:getNumber(spinner.value)))
    unformattedLabel = string.replace(unformattedLabel, "%min%", string.format(minFormat, data:getNumber(spinner.min)))
    unformattedLabel = string.replace(unformattedLabel, "%max%", string.format(maxFormat, data:getNumber(spinner.max)))
    unformattedLabel = string.replace(unformattedLabel, "%increment%", string.format(incrementFormat, data:getNumber(spinner.increment)))

    MoonDust.replaceText(widget, unformattedLabel, 0)
end

local function init(widget)
    updateLabel(widget)
    MoonDust.replaceStyleText(widget, pickStyle(widget, false))
end

local function verifyBounds(widget)
    local data = widget:customData()
    local value = data:getNumber(spinner.value)
    local min = data:getNumber(spinner.min)
    local max = data:getNumber(spinner.max)

    if max <= min then
        return
    end

    if value > max then
        data:setNumber(spinner.value, max)
    end
    if value < min then
        data:setNumber(spinner.value, min)
    end
end

local function dataChanged(widget, changed)
    if changed.key == spinner.value or changed.key == spinner.min or changed.key == spinner.max then
        verifyBounds(widget)
    end
    if changed.key == spinner.value or changed.key == spinner.min or changed.key == spinner.max or changed.key == spinner.increment then
        updateLabel(widget)
    end
end

events.onInit:register(init)
events.onDataChanged:register(dataChanged)
events.onEnableChange:register(enableChange)
