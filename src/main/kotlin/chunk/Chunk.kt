package chunk

import AABB
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL40.glEnableVertexAttribArray
import org.lwjgl.system.MemoryUtil
import render.Renderable
import render.mesh.IndicesVBO
import render.mesh.MeshBuilder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.random.Random

class Chunk(val originX: Int, val originY: Int, val originZ: Int) : Renderable() {
    val r = Random.nextInt(0, 255)
    val builder = MeshBuilder()
    var state = ChunkState.NONE
    val position = Vector3f(originX.toFloat(), originY.toFloat(), originZ.toFloat())
    val blocks: Array<Array<Array<Int>>> = Array(CHUNK_SIZE) {
        Array(CHUNK_SIZE) {
            Array(CHUNK_SIZE) {
                0
            }
        }
    }

    val aabb = AABB(Vector3f(position), Vector3f(CHUNK_SIZE.toFloat(), CHUNK_SIZE.toFloat(), CHUNK_SIZE.toFloat()).add(position))

    init {
        for (x in 0..<CHUNK_SIZE) {
            for (y in 0..<CHUNK_SIZE) {
                for (z in 0..<CHUNK_SIZE) {
                    if ((x + y + z) % 2 == 0) {
                        blocks[x][y][z] = 1
                    }
                }
            }
        }

        transformationMatrix = Matrix4f().setTranslation(
            (originX * CHUNK_SIZE).toFloat(), (originY * CHUNK_SIZE).toFloat(),
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
        const val CHUNK_SIZE = 32

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