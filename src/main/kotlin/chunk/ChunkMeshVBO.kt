package chunk

import org.lwjgl.opengl.GL40.*
import render.mesh.VBO
import render.mesh.VertexMesh.Companion.getIntBuffer
import java.nio.IntBuffer

class ChunkMeshVBO : VBO(2, 1) {
    override fun flush(data: IntBuffer) {
        glBindBuffer(GL_ARRAY_BUFFER, id)
        glVertexAttribPointer(2, 1, GL_INT, false, 0, 0)
        glEnableVertexAttribArray(2)
        glVertexAttribDivisor(2, 1)
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }
}