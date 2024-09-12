package chunk

import Renderable
import org.joml.Matrix4f
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL40.glEnableVertexAttribArray
import org.lwjgl.system.MemoryUtil
import render.mesh.IndicesVBO
import render.mesh.MeshBuilder
import java.nio.FloatBuffer
import java.nio.IntBuffer


class Chunk(val originX: Int, val originZ: Int) : Renderable() {
    val builder = MeshBuilder()
    var state = ChunkState.NONE
    val blocks: Array<Array<Array<Int>>> = Array(16) {
        Array(384) {
            Array(16) {
                0
            }
        }
    }

    init {
        for (x in 0..<CHUNK_SIZE) {
            for (y in 0..<384) {
                for (z in 0..<CHUNK_SIZE) {
//                    blocks[x][y][z] = 1
                    if ((x + y + z) % 2 == 0) {
                        blocks[x][y][z] = 1
                    }
                }
            }
        }

        transformationMatrix = Matrix4f().setTranslation(
            (originX * CHUNK_SIZE).toFloat(), 0f,
            (originZ * CHUNK_SIZE).toFloat()
        )

        mesh.bind()
        mesh.getVbo(0, 2).flush(getFloatBuffer(VERTICES))
        mesh.getVbo(1, 2).flush(getFloatBuffer(UVS))
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        val indices = mesh.addVbo(IndicesVBO())
        indices.flush(getIntBuffer(INDICES))
        mesh.addVbo(ChunkMeshVBO())
    }

    companion object {
        const val CHUNK_SIZE = 16

        val VERTICES = floatArrayOf(
            -0.5f, -0.5f,
            0.5f, -0.5f,
            -0.5f, 0.5f,
            0.5f, 0.5f,
        )

        val UVS = floatArrayOf(
            0f, 0f,
            0f, 1f,
            1f, 0f,
            1f, 1f
        )

        val INDICES = intArrayOf(
            0, 1, 2,
            2, 1, 3
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