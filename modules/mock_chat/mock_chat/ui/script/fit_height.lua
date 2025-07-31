local function calcHeight(h, input)
    if input == nil then
        return h
    else
        return h - input
    end
end

local function init(widget)
    local parent = widget:getParent()
    local parentBounds = parent:getComponent("moondust:bounds")
    widget:addComponent("moondust:bounds", {width = widget:getComponent("moondust:bounds").width, height = calcHeight(parentBounds.height, inputArgs)})
end

local function changeSize(widget, newSize)
    local input = inputArgs
    if input == nil then
        input = 0
    end
    widget:addComponent("moondust:bounds", {width = widget:getComponent("moondust:bounds").width, height = calcHeight(newSize.height, input * MoonDust.getPixelScale()) / MoonDust.getPixelScale()})
end

local function changePixelScale(widget, newSize)
    local parent = widget:getParent()
    local parentBounds = parent:getComponent("moondust:bounds")
    widget:addComponent("moondust:bounds", {width = widget:getComponent("moondust:bounds").width, height = calcHeight(parentBounds.height, inputArgs)})
end

events.onInit:register(init)
events.onGlobalWindowSizeChange:register(changeSize)
events.onGlobalPixelScaleChange:register(changePixelScale)