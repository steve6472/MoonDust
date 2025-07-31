local function calcWidth(w)
    if inputArgs == nil then
        return w
    elseif type(inputArgs) == "number" then
        return w * (inputArgs / 100)
    else
        return nil
    end
end

local function init(widget)
    local parent = widget:getParent()
    local parentBounds = parent:getComponent("moondust:bounds")
    widget:addComponent("moondust:bounds", {width = calcWidth(parentBounds.width), height = widget:getComponent("moondust:bounds").height})
end

local function changeSize(widget, newSize)
    widget:addComponent("moondust:bounds", {width = calcWidth(newSize.width) / MoonDust.getPixelScale(), height = widget:getComponent("moondust:bounds").height})
end

local function changePixelScale(widget, newSize)
    local parent = widget:getParent()
    local parentBounds = parent:getComponent("moondust:bounds")
    widget:addComponent("moondust:bounds", {width = calcWidth(parentBounds.width), height = widget:getComponent("moondust:bounds").height})
end

events.onInit:register(init)
events.onGlobalWindowSizeChange:register(changeSize)
events.onGlobalPixelScaleChange:register(changePixelScale)