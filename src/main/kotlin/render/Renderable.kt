package render

import render.mesh.Mesh
import org.joml.Matrix4f

open class Renderable {
    var transformationMatrix = Matrix4f()
    var mesh = Mesh()
}