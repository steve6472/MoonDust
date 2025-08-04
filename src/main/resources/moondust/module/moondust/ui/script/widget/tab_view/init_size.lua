local sides = {
    outer_frame = {
        top = {
            pos = {0, 11},
            bounds = {"100%", "100% - 11"}
        },
        left = {
            pos = {11, 0},
            bounds = {"100% - 11", "100%"}
        }
    },

    content = {
        top = {
            pos = {6, 18},
            bounds = {"100% - 13", "100% - 24"}
        },
        left = {
            pos = {17, 7},
            bounds = {"100% - 24", "100% - 13"}
        }
    }
}
local RADIO_BUTTON_GROUP = "tab_buttons"

local function findGroupMembers(container, group)
    -- Return empty table if container is nil
    if container == nil then return {} end

    local childrenNames = container:getChildrenNames()
    local groupMembers = {}
    for _, childName in ipairs(childrenNames) do
        local child = container:getChild(childName)
        local childRadio = child:getTable("moondust:radio_group")
        if not (childRadio == nil) and childRadio.group == group then
            groupMembers[#groupMembers+1] = child
        end
    end

    return groupMembers
end

local function adjustValues(inputTable, addition, indexToModify)
    -- Create a shallow copy of the input table
    local newTable = {}
    for k, v in pairs(inputTable) do
        newTable[k] = v
    end

    local value = newTable[indexToModify]

    if type(value) == "string" then
        local prefix, num = string.match(value, "^(.-)(%-?%d+)$")
        if num then
            local newNum = tonumber(num) + addition
            newTable[indexToModify] = prefix .. newNum
        end
    elseif type(value) == "number" then
        newTable[indexToModify] = value + addition
    end

    return newTable
end

local function init(widget)
    local side = widget:getTable("moondust:tab_view").side

    local left = 0
    if (side == "left") then
        local buttons = findGroupMembers(widget, RADIO_BUTTON_GROUP)
        for _, v in pairs(buttons) do
            left = math.max(left, v:getComponent("moondust:bounds").width)
        end
        left = left - 13

        for _, v in pairs(buttons) do
            local vBounds = v:getComponent("moondust:bounds")
            vBounds.width = left + 13
            v:addComponent("moondust:bounds", vBounds)
            v:addComponent("moondust:clickbox_size", vBounds)
        end
    end

    local outerFrame = widget:getChild("outer_frame")
    outerFrame:setBounds(adjustValues(sides.outer_frame[side].bounds, left, 1))
    outerFrame:setPosition(adjustValues(sides.outer_frame[side].pos, left, 1))

    local content = widget:getChild("content")
    content:setBounds(adjustValues(sides.content[side].bounds, left, 1))
    content:setPosition(adjustValues(sides.content[side].pos, left, 1))

end

events.onInit:register(init)