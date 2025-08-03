local function mouseRelease(widget)
    if (not widget:isFocusable()) then
        warning("Tried to grab focus on unfocusable widget "..widget.getPath())
        return
    end

    MoonDust.focus(widget)
end

events.onMouseRelease:register(mouseRelease)