function init(input)
    --print(core.dump(input))

    return {
        properties = {
            text = {
                type = "string",
                default = input.initial_text
            },
            ghost_text = {
                type = "string",
                default = input.ghost_text
            },
            password = {
                type = "boolean",
                default = input.password
            }
        }
    }
end
