import asset.AssetLoader
import input.Input
import org.lwjgl.glfw.GLFW
import window.Window
import window.WindowHints

class ApplicationWrapper {
    private val window = Window(800, 600, "Minecraft2")
    private val assetLoader = AssetLoader()
    private var shouldClose = false

    fun launch() {
        val hints = WindowHints()
        hints.openglProfile = GLFW.GLFW_OPENGL_CORE_PROFILE
        hints.forwardCompat = GLFW.GLFW_TRUE
        hints.contextVersionMajor = 4
        hints.contextVersionMinor = 1

        window.create(hints)

        GLFW.glfwSwapInterval(0)
        Input.create(window)

        assetLoader.load()
        assetLoader.loadAssets()

        val game = Minecraft(window)
        game.create()

        var lastFrameTime = -1L
        var deltaTime: Float
        var frameCounterStart = 0L
        var fps = 0
        var frames = 0

        while (!shouldClose) {
            val time = System.nanoTime()
            if (lastFrameTime == -1L) lastFrameTime = time

            deltaTime = (time - lastFrameTime) / 1000000000.0f

            lastFrameTime = time

            if (time - frameCounterStart >= 1000000000) {
                fps = frames
                frames = 0
                frameCounterStart = time
                println("FPS: $fps")
            }
            frames++

            if (Input.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
                close()
            }

            window.pollEvents()
            Input.update()
            game.render(deltaTime)
            window.swapBuffers()
        }

        game.destroy()
        destroy()
    }

    private fun destroy() {
        window.destroy()
        Input.destroy()
    }

    private fun close() {
        shouldClose = true
    }
}

fun main() {
    ApplicationWrapper().launch()
}