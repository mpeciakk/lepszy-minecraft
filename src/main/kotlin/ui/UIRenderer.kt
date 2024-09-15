package ui

import imgui.ImGui
import imgui.flag.ImGuiConfigFlags
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import input.Input
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL30.*
import render.Renderer
import window.Window

class UIRenderer(window: Window) : Renderer<UIInstance>() {
    private val imGuiGlfw = ImGuiImplGlfw()
    private val imGuiGl3 = ImGuiImplGl3()

    init {
        ImGui.createContext()

        imGuiGlfw.init(window.id, true)
        imGuiGl3.init("#version 410")

        val io = ImGui.getIO()
        io.iniFilename = null
//        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard)
//        io.addConfigFlags(ImGuiConfigFlags.DockingEnable)
        io.setDisplaySize(window.width.toFloat(), window.height.toFloat())
//
//        io.fonts.addFontDefault()
//        io.fonts.build()
    }

    override fun render(t: UIInstance) {
//        val imGuiIO = ImGui.getIO()
//        val mousePos = Input.mousePosition
//        imGuiIO.setMousePos(mousePos.x, mousePos.y)
//        imGuiIO.setMouseDown(0, Input.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT))
//        imGuiIO.setMouseDown(1, Input.isButtonPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT))
//        imGuiIO.setKeysDown(Input.keys)

        imGuiGlfw.newFrame()
        ImGui.newFrame()

        t.render()

        ImGui.render()
        imGuiGl3.renderDrawData(ImGui.getDrawData())
    }
}