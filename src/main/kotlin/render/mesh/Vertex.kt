package render.mesh

import asset.Texture
import org.joml.Vector2f
import org.joml.Vector3f

data class Vertex(val position: Vector3f, val uvs: Vector2f, val texture: Texture?, val normal: Vector3f) {
    constructor(x: Float, y: Float, z: Float, u: Float, v: Float, texture: Texture?, nx: Float, ny: Float, nz: Float) : this(Vector3f(x, y, z), Vector2f(u, v), texture, Vector3f(nx, ny, nz))
}