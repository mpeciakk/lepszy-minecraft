package render

import chunk.Chunk
import chunk.Chunk.Companion
import chunk.ChunkMeshVBO
import org.joml.Vector3f
import org.lwjgl.opengl.GL40.*
import org.lwjgl.system.MemoryUtil
import render.mesh.IndicesVBO
import render.mesh.Mesh
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.random.Random

class ChunkBufferRenderer(val chunks: List<Chunk>) : Renderer<Chunk>() {
    val mesh = Mesh()
    val depthFBO = glGenFramebuffers()
    val depthTexture = glGenTextures()
    val vbo = glGenBuffers()

    private fun packValues(x: Int, y: Int, z: Int, c: Int): Int {
        return x or (y shl 8) or (z shl 16) or (c shl 24)
    }


    init {
        mesh.bind()
        mesh.getVbo(0, 3).flush(getFloatBuffer(VERTICES))
        glEnableVertexAttribArray(0)
        val indices = mesh.addVbo(IndicesVBO())
        indices.flush(getIntBuffer(INDICES))

        val data = mutableListOf<Int>()

        for (chunk in chunks) {
            data.add(packValues(chunk.originX, chunk.originY, chunk.originZ, chunk.r))
        }

        glBindBuffer(GL_ARRAY_BUFFER,  vbo)
        glVertexAttribPointer(1, 1, GL_INT, false, 0, 0)
        glEnableVertexAttribArray(1)
        glVertexAttribDivisor(1, 1)
        glBufferData(GL_ARRAY_BUFFER, getIntBuffer(data.toIntArray()), GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        mesh.unbind()
    }

    override fun render(t: Chunk) {

    }

    fun render() {
        mesh.bind()
        glDrawElementsInstanced(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0, 2*2*2)
        mesh.unbind()
    }

    companion object {
        const val CHUNK_SIZE = 32

        val VERTICES = floatArrayOf(
            -0.5f, -0.5f, -0.5f,   // v0
            0.5f, -0.5f, -0.5f,   // v1
            0.5f,  0.5f, -0.5f,   // v2
            -0.5f,  0.5f, -0.5f,   // v3
            -0.5f, -0.5f,  0.5f,   // v4
            0.5f, -0.5f,  0.5f,   // v5
            0.5f,  0.5f,  0.5f,   // v6
            -0.5f,  0.5f,  0.5f    // v7
        )

        val INDICES = intArrayOf(
            // Front face
            4, 7, 6,    // Triangle 1
            4, 6, 5,    // Triangle 2

            // Back face
            0, 2, 3,    // Triangle 1
            0, 1, 2,    // Triangle 2

            // Left face
            0, 3, 7,    // Triangle 1
            0, 7, 4,    // Triangle 2

            // Right face
            1, 5, 6,    // Triangle 1
            1, 6, 2,    // Triangle 2

            // Top face
            3, 2, 6,    // Triangle 1
            3, 6, 7,    // Triangle 2

            // Bottom face
            0, 4, 5,    // Triangle 1
            0, 5, 1     // Triangle 2
        )

        fun getIntBuffer(data: IntArray): IntBuffer {
            val buffer = MemoryUtil.memAllocInt(data.size)
            buffer.put(data).flip()
            return buffer
        }

        fun getFloatBuffer(data: FloatArray): FloatBuffer {
            val buffer = MemoryUtil.memAllocFloat(data.size)
            buffer.put(data).flip()
            return buffer
        }
    }
}