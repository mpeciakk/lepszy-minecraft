package input

import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWCursorPosCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback
import util.Destroyable
import window.Window

object Input : Destroyable {
    val mousePosition = Vector2f()
    val prevMousePosition = Vector2f()
    val deltaMousePosition = Vector2f()

    val keys = BooleanArray(GLFW_KEY_LAST)
    val buttons = BooleanArray(GLFW_MOUSE_BUTTON_LAST)

    private val keyCallback = object : GLFWKeyCallback() {
        override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
            keys[key] = action != GLFW_RELEASE
        }
    }

    private val mouseButtonCallback = object : GLFWMouseButtonCallback() {
        override fun invoke(window: Long, button: Int, action: Int, mods: Int) {
            buttons[button] = action != GLFW_RELEASE
        }
    }

    private val mousePositionCallback = object : GLFWCursorPosCallback() {
        override fun invoke(window: Long, x: Double, y: Double) {
            mousePosition.x = x.toFloat()
            mousePosition.y = y.toFloat()
        }
    }

    fun create(window: Window) {
        glfwSetKeyCallback(window.id, keyCallback)
        glfwSetMouseButtonCallback(window.id, mouseButtonCallback)
        glfwSetCursorPosCallback(window.id, mousePositionCallback)
    }

    fun update() {
        deltaMousePosition.y = mousePosition.x - prevMousePosition.x
        deltaMousePosition.x = mousePosition.y - prevMousePosition.y

        prevMousePosition.x = mousePosition.x
        prevMousePosition.y = mousePosition.y
    }

    fun isKeyPressed(key: Int): Boolean {
        return keys[key]
    }

    fun isButtonPressed(button: Int): Boolean {
        return buttons[button]
    }

    override fun destroy() {
        keyCallback.free()
        mouseButtonCallback.free()
        mousePositionCallback.free()
    }
}