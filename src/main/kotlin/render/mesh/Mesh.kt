package render.mesh

import org.lwjgl.opengl.GL30.*
import util.Destroyable

open class Mesh : Destroyable {
    val vao = glGenVertexArrays()
    val vbos = mutableMapOf<Int, VBO>()

    var elementsCount = 0

    fun addVbo(vbo: VBO): VBO {
        vbos[vbo.attributeNumber] = vbo
        return vbo
    }

    fun getVbo(attributeNumber: Int, size: Int): VBO {
        if (vbos.containsKey(attributeNumber)) {
            return vbos[attributeNumber]!!
        }

        return addVbo(VBO(attributeNumber, size))
    }

    override fun destroy() {
        glDeleteVertexArrays(vao)

        for (vbo in vbos.values) {
            vbo.destroy()
        }
    }

    fun bind() {
        glBindVertexArray(vao)
    }

    fun unbind() {
        glBindVertexArray(0)
    }
}