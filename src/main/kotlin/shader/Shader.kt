package shader

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*
import util.Destroyable
import kotlin.system.exitProcess

class Shader(shader: String) : Destroyable {
    private var program: Int
    private var vertex: Int
    private var fragment: Int
    private val locationCache = HashMap<String, Int>()
    private var matrixBuffer = BufferUtils.createFloatBuffer(16)

    init {
        val glslPreprocessor = GLSLPreprocessor()

        vertex = loadShader(
            """
            #version 410 core
            #define VERTEX
            ${glslPreprocessor.process(shader)}
            """.trim(), GL_VERTEX_SHADER
        )
        fragment = loadShader(
            """
            #version 410 core
            #define FRAGMENT
            ${glslPreprocessor.process(shader)}
            """.trim(), GL_FRAGMENT_SHADER
        )

        program = glCreateProgram()

        glAttachShader(program, vertex)
        glAttachShader(program, fragment)
//        bindAttributes()
        glLinkProgram(program)
        glValidateProgram(program)
    }

    private fun loadShader(source: String, type: Int): Int {
        val shader = glCreateShader(type)

        glShaderSource(shader, source)
        glCompileShader(shader)

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            println(glGetShaderInfoLog(shader, 500))
            println("Could not compile shader!")
            exitProcess(-1)
        }

        return shader
    }

    fun start() {
        glUseProgram(program)
    }

    fun stop() {
        glUseProgram(0)
    }

    private fun getUniformLocation(name: String): Int {
        return if (locationCache.containsKey(name)) {
            locationCache[name]!!
        } else {
            val location = glGetUniformLocation(program, name)
            locationCache[name] = location
            location
        }
    }

    fun loadFloat(name: String, value: Float) {
        glUniform1f(getUniformLocation(name), value)
    }

    fun loadInt(name: String, value: Int) {
        glUniform1i(getUniformLocation(name), value)
    }

    fun loadVector(name: String, vector: Vector3f) {
        glUniform3f(getUniformLocation(name), vector.x, vector.y, vector.z)
    }

    fun loadVector(name: String, vector: Vector2f) {
        glUniform2f(getUniformLocation(name), vector.x, vector.y)
    }

    fun loadVector(name: String, vector: Vector3i) {
        glUniform3i(getUniformLocation(name), vector.x, vector.y, vector.z)
    }

    fun loadBoolean(name: String, value: Boolean) {
        glUniform1f(getUniformLocation(name), (if (value) 1 else 0).toFloat())
    }

    fun loadMatrix(name: String, matrix: Matrix4f) {
        matrixBuffer = matrix.get(matrixBuffer)
        glUniformMatrix4fv(getUniformLocation(name), false, matrixBuffer)
        matrixBuffer.clear()
    }

//    protected abstract fun bindAttributes()

    protected fun bindAttribute(attribute: Int, name: String) {
        glBindAttribLocation(program, attribute, name)
    }

    override fun destroy() {
        stop()
        glDetachShader(program, vertex)
        glDetachShader(program, fragment)
        glDeleteShader(vertex)
        glDeleteShader(fragment)
        glDeleteProgram(program)
    }
}