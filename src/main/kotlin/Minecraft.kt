import asset.AssetManager
import asset.Texture
import camera.Camera
import chunk.Chunk
import chunk.ChunkState
import imgui.ImGui.*
import imgui.flag.ImGuiCond
import imgui.flag.ImGuiConfigFlags
import imgui.flag.ImGuiStyleVar
import imgui.flag.ImGuiWindowFlags
import imgui.internal.ImGui
import org.lwjgl.opengl.GL30.*
import render.ChunkRenderer
import render.Framebuffer
import shader.Shader
import ui.UIInstance
import ui.UIRenderer
import ui.begin
import util.Destroyable
import window.Window

class UI(private val window: Window, private val mainFramebuffer: Framebuffer, private val hzb: HZBOcclusionCulling) : UIInstance() {
    override fun render() {
        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always)
        ImGui.setNextWindowSize(window.width.toFloat(), window.height.toFloat())

        begin("Scene") {
            beginChild("GameRender")
            val width = ImGui.getContentRegionAvail().x
            val height = ImGui.getContentRegionAvail().y

            image(mainFramebuffer.texture, width / 2, height / 2, 0f, 1f, 1f, 0f)
            image(hzb.framebuffer.texture, width / 2, height / 2, 0f, 1f, 1f, 0f)

            endChild()
        }
    }
}

class Minecraft(private val window: Window) : Destroyable {
    private val renderer = ChunkRenderer()
    private val chunks = mutableListOf<Chunk>()
    private val shader = AssetManager[Shader::class.java, "instanced_test"]
    private val texture2 = AssetManager[Texture::class.java, "cobblestone"]
    private val uiRenderer = UIRenderer(window)
    private lateinit var hzb: HZBOcclusionCulling
    private lateinit var ui: UI

    private val mainFramebuffer = Framebuffer(window.width, window.height, GL_RGB, GL_COLOR_ATTACHMENT0, GL_UNSIGNED_BYTE)

    fun create() {
        for (x in 0..<2) {
            for (y in 0..<2) {
                for (z in 0..<2) {
                    val chunk = Chunk(x, y, z)
                    chunk.state = ChunkState.TO_BUILD
                    chunks.add(chunk)
                }
            }
        }


        val camera = Camera(window.width, window.height)
        camera.makeCurrent()

//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)

        hzb = HZBOcclusionCulling(window.width, window.height, chunks)
        ui = UI(window, mainFramebuffer, hzb)


//        glEnable(GL_CULL_FACE)
//        glCullFace(GL_BACK)

    }

    fun render(deltaTime: Float) {
        glClearColor(0f, 0f, 0f, 1f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        Camera.current.update(deltaTime)

        hzb.renderSceneForHZB()

        mainFramebuffer.bind()
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        shader.start()
        shader.loadMatrix("projectionMatrix", Camera.current.projectionMatrix)
        shader.loadMatrix("viewMatrix", Camera.current.viewMatrix)



        for (chunk in chunks) {
//            if (hzb.testOcclusion(chunk.aabb)) {
                glBindTexture(GL_TEXTURE_2D, texture2.id)
                shader.loadMatrix("transformationMatrix", chunk.transformationMatrix)
                renderer.render(chunk)
                glBindTexture(GL_TEXTURE_2D, 0)
//            }
        }


        shader.stop()
        mainFramebuffer.unbind()

        uiRenderer.render(ui)
    }

    override fun destroy() {

    }
}