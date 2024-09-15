package ui.value

import imgui.type.ImBoolean

class BooleanValue(default: Boolean = false) {
    val imValue = ImBoolean(default)
    val value
        get() = imValue.get()
}