package render.mesh

class MeshBuilder {
    val vertices = mutableListOf<Vertex>()
    val indices = mutableListOf<Int>()

    fun addVertex(vertex: Vertex) {
        vertices.add(vertex)
    }

    fun addIndex(index: Int) {
        indices.add(index)
    }

    fun setVertices(list: List<Vertex>) {
        vertices.clear()
        vertices.addAll(list)
    }

    fun setIndices(list: List<Int>) {
        indices.clear()
        indices.addAll(list)
    }

    fun drawCube(
        x: Float,
        y: Float,
        z: Float,
        w: Float,
        h: Float,
        d: Float,
        renderNorth: Boolean,
        renderSouth: Boolean,
        renderEast: Boolean,
        renderWest: Boolean,
        renderUp: Boolean,
        renderDown: Boolean,
    ) {
        if (renderNorth) {
            drawQuad(
                Vertex(x, y, z, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y, z, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y + h, z, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z, 1f, 0f, null, 0f, 0f, 0f)
            )
        }

        if (renderSouth) {
            drawQuad(
                Vertex(x, y, z + d, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y, z + d, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y + h, z + d, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z + d, 1f, 0f, null, 0f, 0f, 0f)
            )
        }

        if (renderEast) {
            drawQuad(
                Vertex(x + w, y, z, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y, z + d, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z + d, 1f, 0f, null, 0f, 0f, 0f)
            )
        }

        if (renderWest) {
            drawQuad(
                Vertex(x, y, z, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y, z + d, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y + h, z, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x, y + h, z + d, 1f, 0f, null, 0f, 0f, 0f)
            )
        }

        if (renderUp) {
            drawQuad(
                Vertex(x, y + h, z, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y + h, z + d, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x + w, y + h, z + d, 1f, 0f, null, 0f, 0f, 0f)
            )
        }

        if (renderDown) {
            drawQuad(
                Vertex(x, y, z, 0f, 1f, null, 0f, 0f, 0f),
                Vertex(x + w, y, z, 1f, 1f, null, 0f, 0f, 0f),
                Vertex(x, y, z + d, 0f, 0f, null, 0f, 0f, 0f),
                Vertex(x + w, y, z + d, 1f, 0f, null, 0f, 0f, 0f)
            )
        }
    }

    fun drawQuad(a: Vertex, b: Vertex, c: Vertex, d: Vertex) {
        val n = vertices.size

        vertices.addAll(listOf(a, b, c, d))

        indices.addAll(
            listOf(
                n + 0, n + 1, n + 2,
                n + 3, n + 2, n + 1
            )
        )
    }
}