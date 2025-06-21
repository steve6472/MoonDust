local legacyColorMap = {
    ["§0"] = "mcfont:%s/black",
    ["§1"] = "mcfont:%s/dark_blue",
    ["§2"] = "mcfont:%s/dark_green",
    ["§3"] = "mcfont:%s/dark_aqua",
    ["§4"] = "mcfont:%s/dark_red",
    ["§5"] = "mcfont:%s/dark_purple",
    ["§6"] = "mcfont:%s/gold",
    ["§7"] = "mcfont:%s/gray",
    ["§8"] = "mcfont:%s/dark_gray",
    ["§9"] = "mcfont:%s/blue",
    ["§a"] = "mcfont:%s/green",
    ["§b"] = "mcfont:%s/aqua",
    ["§c"] = "mcfont:%s/red",
    ["§d"] = "mcfont:%s/light_purple",
    ["§e"] = "mcfont:%s/yellow",
    ["§f"] = "mcfont:%s/white"
}

-- §k = magic
-- §l = bold
-- §m = strikethrough
-- §n = underline
-- §o = italic
-- §r = reset/normal

-- split a string
local function split(text, delimiter)
    local result = {}
    local from = 1
    local delim_from, delim_to = string.find(text, delimiter, from)
    while delim_from do
        table.insert(result, string.sub(text, from, delim_from - 1))
        from                 = delim_to + 1
        delim_from, delim_to = string.find(text, delimiter, from)
    end
    table.insert(result, string.sub(text, from))
    return result
end

local function getStyle(isBold, isItalic)
    if (isBold and isItalic) then return "bold_italic" end
    if (isBold) then return "bold" end
    if (isItalic) then return "italic" end
    return "normal"
end

local function createColoredParts(rawText)
    local splitText = split(rawText, "§")
    for i, text in ipairs(splitText) do
        splitText[i] = "§"..text
        if (i > 1) then
            splitText[i - 1] = splitText[i]
        end
    end
    table.remove(splitText, #splitText)

    local theIndex = 1;
    local newSplitText = {}
    for _, text in ipairs(splitText) do
        local firstTwo = string.sub(text, 1, 3)
        if (string.sub(text, 1, 2) == "§" and firstTwo ~= text) then
            newSplitText[theIndex] = firstTwo
            theIndex = theIndex + 1
            newSplitText[theIndex] = string.sub(text, 4, -1)
            theIndex = theIndex + 1
        else
            newSplitText[theIndex] = text
            theIndex = theIndex + 1
        end
    end

    local isBold = false
    local isItalic = false
    local color = "§f"

    theIndex = 1
    local parts = {}

    for _, text in ipairs(newSplitText) do
        local format = string.sub(text, 1, 3)
        if (string.sub(text, 1, 2) == "§") then
            if (format == "§l") then isBold = true end
            if (format == "§o") then isItalic = true end
            if (legacyColorMap[format]) ~= nil then
                color = legacyColorMap[format]
                isBold = false
                isItalic = false
            end
        else
            parts[theIndex] = {
                text = text,
                style = string.replace(color, "%s", getStyle(isBold, isItalic))
            }
            theIndex = theIndex + 1
        end
    end

    return parts
end

local function init(widget)
    local tooltipInfo = widget:getTable("mcfont:tooltip")
    if (tooltipInfo == nil) then return end

    local title = widget:getChild("title")
    local titleText = title:getComponent("moondust:text")

    titleText.text.parts = createColoredParts(tooltipInfo.title)
    title:addComponent("moondust:text", titleText)

    local lore = widget:getChild("lore")
    local loreText = lore:getComponent("moondust:text")

    local loreTextJoined = ""
    for i, text in ipairs(tooltipInfo.lore) do
        loreTextJoined = loreTextJoined..text
        if (i < #tooltipInfo.lore) then
            loreTextJoined = loreTextJoined.."\n"
        end
    end
    loreText.text.parts = createColoredParts(loreTextJoined)
    lore:addComponent("moondust:text", loreText)
end

events.onInit:register(init)