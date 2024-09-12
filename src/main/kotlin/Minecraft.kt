import asset.AssetManager
import asset.Texture
import chunk.Chunk
import chunk.ChunkState
import org.joml.Matrix4f
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL33.glVertexAttribDivisor
import org.lwjgl.opengl.GL40.GL_DRAW_INDIRECT_BUFFER
import org.lwjgl.system.MemoryUtil
import render.ChunkRenderer
import render.mesh.IndicesVBO
import render.mesh.Mesh
import shader.Shader
import util.Destroyable
import window.Window
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Minecraft(private val window: Window) : Destroyable {
    private val renderer = ChunkRenderer()
    private val chunks = mutableListOf<Chunk>()
    private val shader = AssetManager[Shader::class.java, "instanced_test"]
    private val mesh = Mesh()
    private val texture = AssetManager[Texture::class.java, "cobblestone"]

    private fun packValues(x: Int, y: Int, z: Int, n: Int): Int {
        return x or (y shl 4) or (z shl 8) or (n shl 12)
    }

    fun create() {
        for (x in 0..<4) {
            for (z in 0..<4) {
                val chunk = Chunk(x, z)
                chunk.state = ChunkState.TO_BUILD
                chunks.add(chunk)
            }
        }

        val camera = Camera(window.width, window.height)
        camera.makeCurrent()

//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)

        glEnable(GL_DEPTH_TEST)
//        glEnable(GL_CULL_FACE)
//        glCullFace(GL_BACK)
    }

    fun render(deltaTime: Float) {
        glClearColor(0f, 0f, 0f, 1f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        Camera.current.update(deltaTime)

        shader.start()

        shader.loadMatrix("projectionMatrix", Camera.current.projectionMatrix)
        shader.loadMatrix("viewMatrix", Camera.current.viewMatrix)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, texture.id)

        for (chunk in chunks) {
            shader.loadMatrix("transformationMatrix", chunk.transformationMatrix)
            renderer.render(chunk)
        }

        shader.stop()
    }

    override fun destroy() {

    }
}