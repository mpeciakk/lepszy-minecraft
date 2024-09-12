package window

import org.lwjgl.glfw.GLFW.*

class WindowHints {
    var resizable = GLFW_TRUE
    var visible = GLFW_TRUE
    var decorated = GLFW_TRUE
    var focused = GLFW_TRUE
    var autoIconify = GLFW_TRUE
    var floating = GLFW_FALSE
    var maximized = GLFW_FALSE
    var centerCursor = GLFW_TRUE
    var transparentFramebuffer = GLFW_FALSE
    var focusOnShow = GLFW_TRUE
    var scaleToMonitor = GLFW_FALSE
    var redBits = 8
    var greenBits = 8
    var blueBits = 8
    var alphaBits = 8
    var depthBits = 24
    var stencilBits = 8
    var accumRedBits = 0
    var accumGreenBits = 0
    var accumBlueBits = 0
    var accumAlphaBits = 0
    var auxBuffers = 0
    var samples = 0
    var refreshRate = GLFW_DONT_CARE
    var stereo = GLFW_FALSE
    var srgbCapable = GLFW_FALSE
    var doubleBuffer = GLFW_TRUE
    var clientApi = GLFW_OPENGL_API
    var contextCreationApi = GLFW_NATIVE_CONTEXT_API
    var contextVersionMajor = 1
    var contextVersionMinor = 0
    var contextRobustness = GLFW_NO_ROBUSTNESS
    var contextReleaseBehaviour = GLFW_ANY_RELEASE_BEHAVIOR
    var forwardCompat = GLFW_FALSE
    var debugContext = GLFW_FALSE
    var openglProfile = GLFW_OPENGL_ANY_PROFILE
    var cocoaRetinaFramebuffer = GLFW_TRUE
    var cocoaFrameName = ""
    var cocoaGraphicsSwitching = GLFW_FALSE
    var X11ClassName = ""
    var X11InstanceName = ""
}