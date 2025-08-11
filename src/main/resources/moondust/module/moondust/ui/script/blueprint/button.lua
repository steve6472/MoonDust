function init(input)
    --print(core.dump(input))

    local onPress
    if type(input.on_press) == "table" then
        onPress = {
            script = input.on_press.script,
            input = input.on_press.input
        }
    elseif type(input.on_press) == "string" then
        onPress = {
            script = input.on_press
        }
    end

    return {
        widget = "moondust:button",
        tables = {
            ["moondust:button"] = {
                label = input.label
            }
        },

        scripts = {
            on_press = onPress
        }
    }
end
