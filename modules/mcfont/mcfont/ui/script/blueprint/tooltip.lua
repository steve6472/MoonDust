local function shiftIndices(tbl)
    local newTbl = {}
    for k, v in pairs(tbl) do
        newTbl[k + 1] = v
    end
    return newTbl
end

function init(input)
    return {
        tables = {
            ["mcfont:tooltip"] = {
                title = input.title,
                lore = shiftIndices(input.lore)
            }
        }
    }
end
