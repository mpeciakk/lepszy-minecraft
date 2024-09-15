package camera

import input.Input
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import kotlin.math.cos
import kotlin.math.sin

class Camera(width: Int, height: Int) {
    val projectionMatrix: Matrix4f
    val viewMatrix = Matrix4f()

    val position = Vector3f(0f, 0f, 0f)
    private val rotation = Vector3f(0f, 0f, 0f)

    init {
        val aspectRatio = width.toFloat() / height.toFloat()
        projectionMatrix = Matrix4f().perspective(
            Math.toRadians(FOV.toDouble()).toFloat(), aspectRatio,
            NEAR_PLANE, FAR_PLANE
        )
    }

    fun update(deltaTime: Float) {
        val cameraMovement = Vector3f()
        cameraMovement.z = if (Input.isKeyPressed(GLFW_KEY_W)) {
            -1f
        } else if (Input.isKeyPressed(GLFW_KEY_S)) {
            1f
        } else {
            0f
        }

        cameraMovement.x = if (Input.isKeyPressed(GLFW_KEY_A)) {
            -1f
        } else if (Input.isKeyPressed(GLFW_KEY_D)) {
            1f
        } else {
            0f
        }

        cameraMovement.y = if (Input.isKeyPressed(GLFW_KEY_SPACE)) {
            1f
        } else if (Input.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            -1f
        } else {
            0f
        }

        if (cameraMovement.z != 0f) {
            position.x += sin(Math.toRadians(rotation.y.toDouble())).toFloat() * -1f * cameraMovement.z * SPEED * deltaTime
            position.z += cos(Math.toRadians(rotation.y.toDouble())).toFloat() * cameraMovement.z * SPEED * deltaTime
        }

        if (cameraMovement.x != 0f) {
            position.x += sin(Math.toRadians((rotation.y - 90f).toDouble())).toFloat() * -1.0f * cameraMovement.x * SPEED * deltaTime
            position.z += cos(Math.toRadians((rotation.y - 90f).toDouble())).toFloat() * cameraMovement.x * SPEED * deltaTime
        }

        position.y += cameraMovement.y * SPEED * deltaTime

        rotation.x += Input.deltaMousePosition.x * SENSITIVITY
        rotation.y += Input.deltaMousePosition.y * SENSITIVITY

        viewMatrix.identity()
        viewMatrix.rotate(Math.toRadians(rotation.x.toDouble()).toFloat(), Vector3f(1f, 0f, 0f))
        viewMatrix.rotate(Math.toRadians(rotation.y.toDouble()).toFloat(), Vector3f(0f, 1f, 0f))
        val cameraPos = position
        val negativeCameraPos = Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z)
        viewMatrix.translate(negativeCameraPos)
    }

    fun makeCurrent() {
        current = this
    }

    companion object {
        const val FOV = 70f
        const val NEAR_PLANE = 0.1f
        const val FAR_PLANE = 1000f

        const val SENSITIVITY = 0.05f
        const val SPEED = 10f

        lateinit var current: Camera
    }
}