--#noscript

local globalMouseButton = {
    action = {
        press = 1,
        release = 0
    },
    button = {
        left = 0,
        right = 1,
        middle = 2
    },
    mods = {
        shift = 0x0001,
        control = 0x0002,
        alt = 0x0004,
        super = 0x0008,
        capsLock = 0x0010,
        numLock = 0x0020
    }
}

local function testMouseMod(mouseData, mod)
    return bit32.band(mouseData.mods, mod) == mod
end