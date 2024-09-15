package render

import camera.Camera
import chunk.Chunk
import chunk.Chunk.Companion.CHUNK_SIZE
import chunk.ChunkBuilderThreadPool
import chunk.ChunkState
import org.lwjgl.opengl.GL40.*
import render.mesh.VertexMesh.Companion.getIntBuffer

class ChunkRenderer : Renderer<Chunk>() {
    private val builderPool = ChunkBuilderThreadPool()
    val list = mutableListOf<Int>()

    private fun packValues(x: Int, y: Int, z: Int, n: Int): Int {
        return x or (y shl 5) or (z shl 10) or (n shl 15)
    }

    override fun render(t: Chunk) {


        if (t.state == ChunkState.TO_BUILD) {
            t.state = ChunkState.BUILDING

//            builderPool.submit {
//                val builder = builderPool.getBuilder(t)
//
//                for (x in 0..<16) {
//                    for (y in 0..<384) {
//                        for (z in 0..<16) {
//                            if (t.blocks[x][y][z] == 1) {
//                                val renderNorth = if (z - 1 >= 0) t.blocks[x][y][z - 1] == 0 else true
//                                val renderSouth = if (z + 1 < 16) t.blocks[x][y][z + 1] == 0 else true
//                                val renderEast = if (x + 1 < 16) t.blocks[x + 1][y][z] == 0 else true
//                                val renderWest = if (x - 1 >= 0) t.blocks[x - 1][y][z] == 0 else true
//                                val renderUp = if (y + 1 < 384) t.blocks[x][y + 1][z] == 0 else true
//                                val renderDown = if (y - 1 >= 0) t.blocks[x][y - 1][z] == 0 else true
//
//                                builder.drawCube(
//                                    x.toFloat(),
//                                    y.toFloat(), z.toFloat(), 1f, 1f, 1f, renderNorth, renderSouth, renderEast, renderWest, renderUp, renderDown
//                                )
//                            }
//                        }
//                    }
//                }

            println("building")

            for (x in 0..<CHUNK_SIZE) {
                for (y in 0..<CHUNK_SIZE) {
                    for (z in 0..<CHUNK_SIZE) {
                        if (t.blocks[x][y][z] == 1) {
                            val renderNorth = if (z - 1 >= 0) t.blocks[x][y][z - 1] == 0 else true
                            val renderSouth = if (z + 1 < CHUNK_SIZE) t.blocks[x][y][z + 1] == 0 else true
                            val renderEast = if (x + 1 < CHUNK_SIZE) t.blocks[x + 1][y][z] == 0 else true
                            val renderWest = if (x - 1 >= 0) t.blocks[x - 1][y][z] == 0 else true
                            val renderUp = if (y + 1 < CHUNK_SIZE) t.blocks[x][y + 1][z] == 0 else true
                            val renderDown = if (y - 1 >= 0) t.blocks[x][y - 1][z] == 0 else true

                            if (renderNorth) {
                                list.add(packValues(x, y, z, 1))
                            }

                            if (renderSouth) {
                                list.add(packValues(x, y, z, 0))
                            }

                            if (renderEast) {
                                list.add(packValues(x, y, z, 3))
                            }

                            if (renderWest) {
                                list.add(packValues(x, y, z, 2))
                            }

                            if (renderUp) {
                                list.add(packValues(x, y, z, 4))
                            }

                            if (renderDown) {
                                list.add(packValues(x, y, z, 5))
                            }
                        }
                    }
                }
            }

            t.mesh.elementsCount = list.size

            t.state = ChunkState.BUILT
        }

        if (t.state == ChunkState.BUILT) {

            println("flushing")
//            t.mesh = VertexMesh(builderPool.getBuilderForChunk(t).vertices, builderPool.getBuilderForChunk(t).indices)
//            builderPool.freeBuilderForChunk(t)
//
//            t.elements = list.size
//
//            glBindBuffer(GL_ARRAY_BUFFER, t.vbo)
//            glBufferData(GL_ARRAY_BUFFER, getIntBuffer(list.toIntArray()), GL_STATIC_DRAW)
//            glBindBuffer(GL_ARRAY_BUFFER, 0)
//
//            t.state = ChunkState.READY

            t.mesh.bind()
            t.mesh.getVbo(2, 1).flush(getIntBuffer(list.toIntArray()))
            t.mesh.unbind()

            list.clear()

            t.state = ChunkState.READY
        }

        if (t.state == ChunkState.READY) {
            t.mesh.bind()
            glDrawElementsInstanced(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0, t.mesh.elementsCount)
            t.mesh.unbind()
        }
    }
}