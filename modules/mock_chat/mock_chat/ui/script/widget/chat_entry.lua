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
end

events.onInit:register(init)