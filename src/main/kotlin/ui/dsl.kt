package ui

import imgui.ImGui
import imgui.type.ImBoolean
import ui.value.BooleanValue
import ui.value.Float2Value
import ui.value.Float3Value
import ui.value.FloatValue

inline fun begin(label: String, open: Boolean = true, flags: Int = 0, block: () -> Unit = { }) {
    if (ImGui.begin(label, ImBoolean(open)))
        block()
    ImGui.end()
}

inline fun frame(block: () -> Unit) {
    ImGui.newFrame()
    block()
    ImGui.endFrame()
}

inline fun tooltip(block: () -> Unit) {
    ImGui.beginTooltip()
    block()
    ImGui.endTooltip()
}

inline fun image(
    id: Int,
    width: Float,
    height: Float,
    u0: Float = 0f,
    v0: Float = 0f,
    u1: Float = 0f,
    v1: Float = 0f,
    block: () -> Unit = { }
) {
    ImGui.image(id, width, height, u0, v0, u1, v1)
    block()
}

inline fun button(label: String, block: () -> Unit = { }) {
    if (ImGui.button(label)) {
        block()
    }
}

inline fun checkbox(label: String, value: BooleanValue, block: () -> Unit = { }) {
    if (ImGui.checkbox(label, value.imValue)) {
        block()
    }
}

inline fun sliderFloat3(label: String, value: Float3Value, min: Float, max: Float, block: () -> Unit = { }) {
    if (ImGui.sliderFloat3(label, value.arr, min, max)) {
        block()
    }
}

inline fun sliderFloat2(label: String, value: Float2Value, min: Float, max: Float, block: () -> Unit = { }) {
    if (ImGui.sliderFloat2(label, value.arr, min, max)) {
        block()
    }
}

inline fun sliderFloat(label: String, value: FloatValue, min: Float, max: Float, block: () -> Unit = { }) {
    if (ImGui.sliderFloat(label, value.arr, min, max)) {
        block()
    }
}

inline fun colorPicker(label: String, value: Float3Value, block: () -> Unit = { }) {
    if (ImGui.colorPicker3(label, value.arr)) {
        block()
    }
}

inline fun colorEdit(label: String, value: Float3Value, block: () -> Unit = { }) {
    if (ImGui.colorEdit3(label, value.arr)) {
        block()
    }
}