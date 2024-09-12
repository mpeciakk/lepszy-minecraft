package render.mesh

import org.lwjgl.opengl.GL15.*
import java.nio.IntBuffer

class IndicesVBO : VBO(-1, 1) {
    override fun flush(data: IntBuffer) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW)
    }
}