local function init(widget)
    local textAreaComp = widget:getTable("mcfont:text_area")
    if (textAreaComp == nil) then
        warning("Minecraft Text Area widget is possibly missing \"mcfont:text_area\" blueprint")
        return
    end
end

events.onInit:register(init)