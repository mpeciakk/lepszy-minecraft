package render.mesh

import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer

class VertexMesh(meshVertices: List<Vertex>, meshIndices: List<Int>) : Mesh() {
    init {
        bind()

        val vertices = getVbo(0, 3)
        val uvs = getVbo(1, 2)
        val normals = getVbo(2, 3)
        val indices = addVbo(IndicesVBO())

        val verticesData = FloatArray(meshVertices.size * 3)
        val uvsData = FloatArray(meshVertices.size * 2)
        val normalsData = FloatArray(meshVertices.size * 3)
        val indicesData = meshIndices.toTypedArray()

        var verticesIndex = 0
        var uvsIndex = 0
        var normalsIndex = 0

        for (vertex in meshVertices) {
            verticesData[verticesIndex++] = vertex.position.x
            verticesData[verticesIndex++] = vertex.position.y
            verticesData[verticesIndex++] = vertex.position.z
            uvsData[uvsIndex++] = vertex.uvs.x
            uvsData[uvsIndex++] = vertex.uvs.y
            normalsData[normalsIndex++] = vertex.normal.x
            normalsData[normalsIndex++] = vertex.normal.y
            normalsData[normalsIndex++] = vertex.normal.z
        }

        vertices.flush(getFloatBuffer(verticesData))
        uvs.flush(getFloatBuffer(uvsData))
        normals.flush(getFloatBuffer(normalsData))
        indices.flush(getIntBuffer(indicesData.toIntArray()))

        elementsCount = indicesData.size

        unbind()
    }

    companion object {
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