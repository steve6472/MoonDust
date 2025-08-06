local function init(widget)
    local entryData = widget:getTable("mock_chat:chat_entry")

    local sprites = widget:getComponent("moondust:sprites")
    sprites.icon = entryData.icon_path
    widget:addComponent("moondust:sprites", sprites)

    local username = widget:getChild("username")
    local text = username:getComponent("moondust:text");
    text.text.parts[1].text = entryData.username
    username:addComponent("moondust:text", text)

    local message = widget:getChild("message")
    text = message:getComponent("moondust:text");
    text.text.parts[1].text = entryData.message_body
    message:addComponent("moondust:text", text)

    widget:getChild("hamnurger"):setVisible(widget:internalStates().hovered)
    widget:getChild("delete_entry"):setVisible(widget:internalStates().hovered)
end

events.onInit:register(init)

local function mouseOn(widget)
    widget:getChild("hamnurger"):setVisible(true)
    widget:getChild("delete_entry"):setVisible(true)
end

local function mouseOff(widget)
    widget:getChild("hamnurger"):setVisible(false)
    widget:getChild("delete_entry"):setVisible(false)
end

events.onMouseEnter:register(mouseOn)
events.onMouseLeave:register(mouseOff)
