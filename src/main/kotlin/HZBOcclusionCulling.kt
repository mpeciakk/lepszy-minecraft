import asset.AssetManager
import camera.Camera
import chunk.Chunk
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL32.*
import org.lwjgl.opengl.GL40.glVertexAttribDivisor
import render.ChunkBufferRenderer.Companion.INDICES
import render.ChunkBufferRenderer.Companion.VERTICES
import render.ChunkBufferRenderer.Companion.getFloatBuffer
import render.ChunkBufferRenderer.Companion.getIntBuffer
import render.Framebuffer
import render.mesh.IndicesVBO
import render.mesh.Mesh
import shader.Shader
import kotlin.math.ln

class HZBOcclusionCulling(val width: Int, val height: Int, val chunks: List<Chunk>) {
    val mesh = Mesh()

    val framebuffer = Framebuffer(width, height, GL_DEPTH_COMPONENT, GL_DEPTH_ATTACHMENT, GL_FLOAT)
    private val shader = AssetManager[Shader::class.java, "instanced_test"]
    private val vbo = glGenBuffers()

    private fun packValues(x: Int, y: Int, z: Int, c: Int): Int {
        return x or (y shl 8) or (z shl 16) or (c shl 24)
    }

    init {
        mesh.bind()
        mesh.getVbo(0, 3).flush(getFloatBuffer(VERTICES))
        glEnableVertexAttribArray(0)
        val indices = mesh.addVbo(IndicesVBO())
        indices.flush(getIntBuffer(INDICES))

        val data = mutableListOf<Int>()

        for (chunk in chunks) {
            data.add(packValues(chunk.originX, chunk.originY, chunk.originZ, chunk.r))
        }

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glVertexAttribPointer(1, 1, GL_INT, false, 0, 0)
        glEnableVertexAttribArray(1)
        glVertexAttribDivisor(1, 1)
        glBufferData(GL_ARRAY_BUFFER, getIntBuffer(data.toIntArray()), GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        mesh.unbind()
    }

    fun renderSceneForHZB() {
        framebuffer.bind()
        glClear(GL_DEPTH_BUFFER_BIT or GL_COLOR_BUFFER_BIT)

        shader.start()

        shader.loadMatrix("projectionMatrix", Camera.current.projectionMatrix)
        shader.loadMatrix("viewMatrix", Camera.current.viewMatrix)
        shader.loadMatrix("transformationMatrix", Matrix4f().setTranslation(16f, 16f, 16f).scale(32f))

        mesh.bind()
        glDrawElementsInstanced(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0, 2 * 2 * 2)
        mesh.unbind()

        framebuffer.unbind()

        glBindTexture(GL_TEXTURE_2D, framebuffer.texture)
        glGenerateMipmap(GL_TEXTURE_2D)
        glBindTexture(GL_TEXTURE_2D, 0)

        shader.stop()
    }

    fun transformBoundingBoxToScreenSpace(bbox: AABB, viewMatrix: Matrix4f, projMatrix: Matrix4f): AABB {
        val transformedMin = transformToScreenSpace(bbox.min, viewMatrix, projMatrix)
        val transformedMax = transformToScreenSpace(bbox.max, viewMatrix, projMatrix)
        return AABB(transformedMin, transformedMax)
    }

    fun transformToScreenSpace(point: Vector3f, viewMatrix: Matrix4f, projMatrix: Matrix4f): Vector3f {
        val worldPos = Vector4f(point, 1.0f)
        val clipPos = Matrix4f(projMatrix).mul(Matrix4f(viewMatrix)).transform(worldPos)
        return Vector3f(
            (clipPos.x / clipPos.w * 0.5f + 0.5f) * width,
            (clipPos.y / clipPos.w * 0.5f + 0.5f) * height,
            clipPos.z / clipPos.w
        )
    }

    fun sampleDepthFromHZB(screenSpaceBoundingBox: AABB, mipLevel: Int): Float {
        val minCoord = Vector2f(screenSpaceBoundingBox.min.x / width, screenSpaceBoundingBox.min.y / height)
        val maxCoord = Vector2f(screenSpaceBoundingBox.max.x / width, screenSpaceBoundingBox.max.y / height)

        // Calculate the center of the bounding box in normalized device coordinates (NDC)
        val centerX = (minCoord.x + maxCoord.x) * 0.5f
        val centerY = (minCoord.y + maxCoord.y) * 0.5f

        // Sample the HZB depth texture at this point
        val depthValue = glGetTextureDepthValue(framebuffer.texture, centerX, centerY, mipLevel)

        return depthValue
    }

    fun glGetTextureDepthValue(textureId: Int, u: Float, v: Float, mipLevel: Int): Float {
        // Bind the texture and set the appropriate mip level for sampling
        glBindTexture(GL_TEXTURE_2D, textureId)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, mipLevel)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, mipLevel)

        // Create a float buffer to hold the depth value
        val depthBuffer = BufferUtils.createFloatBuffer(1)

        // Sample the depth value at the (u, v) coordinate
        glReadPixels(
            (u * width).toInt(),
            (v * height).toInt(),
            1, 1,
            GL_DEPTH_COMPONENT, GL_FLOAT, depthBuffer
        )

        glBindTexture(GL_TEXTURE_2D, 0)

        // Return the depth value
        return depthBuffer.get(0)
    }

    fun calculateMipLevels(width: Int, height: Int): Int {
        return 1 + (ln(Math.max(width, height).toDouble()) / Math.log(2.0)).toInt()
    }

    fun testOcclusion(modelBoundingBox: AABB): Boolean {
        // Step 1: Transform the bounding box to screen space
        val screenSpaceBoundingBox = transformBoundingBoxToScreenSpace(
            modelBoundingBox,
            Camera.current.viewMatrix,
            Camera.current.projectionMatrix
        )

        // Step 2: Iterate through the mipmap levels of the HZB texture
        val mipLevels = calculateMipLevels(width, height)
        for (level in 0 until mipLevels) {
            // Bind the HZB texture at the current mipmap level
            glBindTexture(GL_TEXTURE_2D, framebuffer.texture)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, level)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, level)

            // Step 3: Sample depth from the HZB texture at this mip level
            val depthBufferSample = sampleDepthFromHZB(screenSpaceBoundingBox, level)


            // Step 4: Compare bounding box depth with sampled HZB depth
            if (modelBoundingBox.min.z > depthBufferSample) {
                // Object is occluded at this level
                return true
            }
        }
        // Step 5: If none of the levels occlude the object, it's visible
        return false
    }
}