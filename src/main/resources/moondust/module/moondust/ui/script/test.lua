--#include include/prefix

local function mousePressTest(widget, event)
    print(prefix().."Pressed: "..widget:getName()..", "..widget:getPath())
    local t = widget:getComponent("sprites")
    print(core.dump(t))
    t["hover"] = "moondust:generic_dark_pixel/generic/focus/sharper"
    widget:addComponent("sprites", t)
end

local function dataChange(widget, event)
    --print("Widget: "..core.dump(widget))
    print("Event: "..core.dump(event))
end

events.onMousePress:register(mousePressTest)
--events.onDataChanged:register(dataChange)