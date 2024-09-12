package render.mesh

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.system.MemoryUtil.memFree
import util.Destroyable
import java.nio.FloatBuffer
import java.nio.IntBuffer

open class VBO(val attributeNumber: Int, private val size: Int) : Destroyable {
    val id = glGenBuffers()

    fun flush(data: FloatBuffer) {
        glBindBuffer(GL_ARRAY_BUFFER, id)
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW)
        glVertexAttribPointer(attributeNumber, size, GL_FLOAT, false, 0, 0)
        glBindBuffer(GL_ARRAY_BUFFER,0)
        memFree(data)
    }

    open fun flush(data: IntBuffer) {
        glBindBuffer(GL_ARRAY_BUFFER, id)
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW)
        glVertexAttribPointer(attributeNumber, size, GL_INT, false, 0, 0)
        glBindBuffer(GL_ARRAY_BUFFER,0)
        memFree(data)
    }

    override fun destroy() {
        glDeleteBuffers(id)
    }
}