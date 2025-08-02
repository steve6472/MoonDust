--#include moondust:widget/keys/generic
--#include moondust:widget/util

local compKey = "moondust:radio_group"
local currentSprite = "moondust:current_sprite"

local function pickSprite(enabled, checked)
    if checked then
        if enabled then
            return "selected"
        else
            return "disabled"
        end
    else
        if enabled then
            return "unselected"
        else
            return "disabled"
        end
    end
end

local function pickRenderOrder(checked)
    if checked then
        return {
            widget = "outer_frame",
            order = "above"
        }
    else
        return {
            widget = "outer_frame",
            order = "below"
        }
    end
end

local function findGroupMembers(container, group)
    -- Return empty table if container is nil
    if container == nil then return {} end

    local childrenNames = container:getChildrenNames()
    local groupMembers = {}
    for _, childName in ipairs(childrenNames) do
        local child = container:getChild(childName)
        local childRadio = child:getTable(compKey)
        if not (childRadio == nil) and childRadio.group == group.group then
            groupMembers[#groupMembers+1] = child
        end
    end

    return groupMembers
end

local function enableChange(widget)
    local group = widget:getTable(compKey)
    if group == nil then
        group = {selected = false}
    end

    widget:addComponent(currentSprite, pickSprite(widget:isEnabled(), group.selected))
    --MoonDust.replaceStyleText(widget, pickStyle(widget, false))
end

local function init(widget)
    --MoonDust.replaceStyleText(widget, pickStyle(widget, false))
    local group = widget:getTable(compKey)
    if group == nil then return end

    local parent = widget:getParent()
    local groupMembers = findGroupMembers(parent, group)

    -- Find and log if multiple radio buttons are selected by default
    local warningLogged = false
    for _, child in pairs(groupMembers) do
        local childGroup = child:getTable(compKey)

        if childGroup.selected and group.selected then
            group.selected = false
            widget:setTable(compKey, group)
            if not warningLogged then
                warning("Found multiple by-default selected radio widgets in group '"..group.group.."'")
                warningLogged = true
            end
        end
    end

    --print("Picking style '"..pickSprite(widget:isEnabled(), group.selected).."' Because widget enabled: "..tostring(widget:isEnabled()).." and group selected: "..tostring(group.selected))
    widget:addComponent(currentSprite, pickSprite(widget:isEnabled(), group.selected))
    widget:addComponent("render_order", pickRenderOrder(group.selected))

    -- do what button_change_content does
    if group.selected then
        local child =
        {
            widget = group.label,
            name = "view_content",
            position = {0, 0},
            bounds = {"100%", "100%"}
        }
        local content = widget:getParent():getChild("content")

        for _, v in ipairs(content:getChildrenNames()) do
            content:removeChild(v)
        end

        content:addChildren({child})
    end
end

local function dataChanged(widget, changed)
    -- Only activate when the radio_button table changed
    if (changed.key ~= compKey) then return end
    -- Only run if selected was set to true
    if (changed.new.selected ~= true or changed.previous.selected ~= false) then return end

    local group = widget:getTable(compKey)
    if group == nil then return end

    local groupMembers = findGroupMembers(widget:getParent(), group)

    -- Iterate over all widgets with the same radio_group key
    for _, child in pairs(groupMembers) do
        -- Simply change sprite of the widget, its selected value was already updated (the widget that triggered the change)
        if child:getName() == widget:getName() then
            widget:addComponent(currentSprite, pickSprite(widget:isEnabled(), group.selected))
            widget:addComponent("render_order", pickRenderOrder(group.selected))
        -- Update sprite & selected value (all other widgets in the group)
        else
            local childGroup = child:getTable(compKey)
            childGroup.selected = false
            child:setTable(compKey, childGroup)
            child:addComponent(currentSprite, pickSprite(child:isEnabled(), childGroup.selected))
            child:addComponent("render_order", pickRenderOrder(childGroup.selected))
        end
    end
end

local function mouseRelease(widget)
    if not buttonTest(widget) then return end

    local group = widget:getTable(compKey)
    if group == nil then return end
    if group.selected == true then return end

    group.selected = true
    widget:setTable(compKey, group)
end

events.onInit:register(init)
events.onDataChanged:register(dataChanged)
events.onEnableChange:register(enableChange)
events.onMouseRelease:register(mouseRelease)
