package window

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.system.MemoryStack
import util.Destroyable

class Window(val width: Int, val height: Int, val title: String) : Destroyable {
    var id = 0L

    var shouldClose: Boolean
        get() = glfwWindowShouldClose(id)
        set(value) = glfwSetWindowShouldClose(id, value)

    init {
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }
    }

    fun create(hints: WindowHints) {
        applyWindowHints(hints)

        id = glfwCreateWindow(width, height, title, 0, 0)

        if (id == 0L) {
            throw IllegalStateException("Failed to create GLFW window!")
        }

        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)
            glfwGetWindowSize(id, pWidth, pHeight)
            val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
            glfwSetWindowPos(
                id,
                (videoMode.width() - pWidth.get(0)) / 2,
                (videoMode.height() - pHeight.get(0)) / 2
            )
        }

        glfwMakeContextCurrent(id)
        glfwShowWindow(id)

        glfwSwapInterval(1)

        val cap = GL.createCapabilities()
    }

    private fun applyWindowHints(hints: WindowHints) {
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_RESIZABLE, hints.resizable)
        glfwWindowHint(GLFW_VISIBLE, hints.visible)
        glfwWindowHint(GLFW_DECORATED, hints.decorated)
        glfwWindowHint(GLFW_FOCUSED, hints.focused)
        glfwWindowHint(GLFW_AUTO_ICONIFY, hints.autoIconify)
        glfwWindowHint(GLFW_FLOATING, hints.floating)
        glfwWindowHint(GLFW_MAXIMIZED, hints.maximized)
        glfwWindowHint(GLFW_CENTER_CURSOR, hints.centerCursor)
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, hints.transparentFramebuffer)
        glfwWindowHint(GLFW_FOCUS_ON_SHOW, hints.focusOnShow)
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, hints.scaleToMonitor)
        glfwWindowHint(GLFW_RED_BITS, hints.redBits)
        glfwWindowHint(GLFW_GREEN_BITS, hints.greenBits)
        glfwWindowHint(GLFW_BLUE_BITS, hints.blueBits)
        glfwWindowHint(GLFW_ALPHA_BITS, hints.alphaBits)
        glfwWindowHint(GLFW_DEPTH_BITS, hints.depthBits)
        glfwWindowHint(GLFW_STENCIL_BITS, hints.stencilBits)
        glfwWindowHint(GLFW_ACCUM_RED_BITS, hints.accumRedBits)
        glfwWindowHint(GLFW_ACCUM_GREEN_BITS, hints.accumGreenBits)
        glfwWindowHint(GLFW_ACCUM_BLUE_BITS, hints.accumBlueBits)
        glfwWindowHint(GLFW_ACCUM_ALPHA_BITS, hints.accumAlphaBits)
        glfwWindowHint(GLFW_AUX_BUFFERS, hints.auxBuffers)
        glfwWindowHint(GLFW_SAMPLES, hints.samples)
        glfwWindowHint(GLFW_REFRESH_RATE, hints.refreshRate)
        glfwWindowHint(GLFW_STEREO, hints.stereo)
        glfwWindowHint(GLFW_SRGB_CAPABLE, hints.srgbCapable)
        glfwWindowHint(GLFW_DOUBLEBUFFER, hints.doubleBuffer)
        glfwWindowHint(GLFW_CLIENT_API, hints.clientApi)
        glfwWindowHint(GLFW_CONTEXT_CREATION_API, hints.contextCreationApi)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, hints.contextVersionMajor)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, hints.contextVersionMinor)
        glfwWindowHint(GLFW_CONTEXT_ROBUSTNESS, hints.contextRobustness)
        glfwWindowHint(GLFW_CONTEXT_RELEASE_BEHAVIOR, hints.contextReleaseBehaviour)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, hints.forwardCompat)
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, hints.debugContext)
        glfwWindowHint(GLFW_OPENGL_PROFILE, hints.openglProfile)
        glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, hints.cocoaRetinaFramebuffer)
        glfwWindowHintString(GLFW_COCOA_FRAME_NAME, hints.cocoaFrameName)
        glfwWindowHint(GLFW_COCOA_GRAPHICS_SWITCHING, hints.cocoaGraphicsSwitching)
        glfwWindowHintString(GLFW_X11_CLASS_NAME, hints.X11ClassName)
        glfwWindowHintString(GLFW_X11_INSTANCE_NAME, hints.X11InstanceName)
    }

    fun pollEvents() {
        glfwPollEvents()
    }

    fun swapBuffers() {
        glfwSwapBuffers(id)
    }

    override fun destroy() {
        glfwDestroyWindow(id)
        glfwTerminate()
    }
}