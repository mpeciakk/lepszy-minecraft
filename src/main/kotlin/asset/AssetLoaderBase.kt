package asset

import org.lwjgl.BufferUtils
import shader.Shader
import java.io.*
import java.net.URISyntaxException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import javax.imageio.ImageIO

open class AssetLoaderBase {
    private val queue: Queue<Pair<String, Class<*>>> = ArrayDeque()
    protected val deserializers = mutableMapOf<Class<*>, Deserializer<*>>()

    init {
        deserializers[Shader::class.java] = object : Deserializer<Shader>() {
            override fun deserialize(name: String): Shader {
                return Shader(getTextFile("/shaders/$name.glsl"))
            }
        }

        deserializers[Texture::class.java] = object : Deserializer<Texture>() {
            override fun deserialize(name: String): Texture {
                val image = ImageIO.read(getFileStream("/textures/$name.png"))

                val width = image.width
                val height = image.height

                val pixelsRaw = image.getRGB(0, 0, width, height, null, 0, height)

                val pixels = BufferUtils.createByteBuffer(width * height * 4)

                try {
                    for (i in 0..<width) {
                        for (j in 0..<height) {
                            val pixel = pixelsRaw[i * width + j]
                            pixels.put((pixel shr 16 and 0xFF).toByte())
                            pixels.put((pixel shr 8 and 0xFF).toByte())
                            pixels.put((pixel and 0xFF).toByte())
                            pixels.put((pixel shr 24 and 0xFF).toByte())
                        }
                    }
                } catch (e: ArrayIndexOutOfBoundsException) {
                    pixels.put(0x88.toByte())
                    pixels.put(0x88.toByte())
                    pixels.put(0x88.toByte())
                    pixels.put(0x00.toByte())
                }

                pixels.flip()

                return Texture(width, height, pixels)
            }
        }
    }

    fun queueAsset(name: String, assetType: Class<*>) {
        queue.add(Pair(name, assetType))
    }

    fun queueAll(path: String, assetType: Class<*>) {
        for (file in getFiles(path)) {
            queueAsset(file.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0], assetType)
        }
    }

    fun loadAssets() {
        val n = queue.size
        for (i in 0..<n) {
            val pair = queue.poll()

            val deserializer = deserializers[pair.second] ?: error("Can't find deserializer for asset of type ${pair.second}")
            val asset = deserializer.deserialize(pair.first) ?: error("Couldn't deserialize asset ${pair.first}")

            AssetManager.register(pair.second, pair.first, asset)
            println("Loaded asset " + (i + 1) + " of " + n)
        }
    }

    fun getFiles(path: String): List<String> {
        val results = mutableListOf<String>()
        var files = arrayOf<File>()

        try {
            files = File(getResource(path).toURI()).listFiles() ?: error("Can't find any files in $path")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        for (file in files) {
            if (file.isFile) {
                results.add(file.name)
            }
        }

        return results
    }

    fun getTextFile(path: String): String {
        try {
            InputStreamReader(getFileStream(path), StandardCharsets.UTF_8).use { streamReader ->
                BufferedReader(streamReader).use { reader ->
                    val builder = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        builder.append(line).append("\n")
                    }
                    return builder.toString()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        throw IllegalArgumentException("Can't read file! ($path)")
    }

    fun getFileStream(originalPath: String): InputStream {
        var path = originalPath

        if (!path.startsWith("/")) {
            path = "/$path"
        }

        val url = getResource(path)

        return url.openStream() ?: error("Failed opening input stream for file $path")
    }

    fun getResource(path: String): URL {
        return AssetLoaderBase::class.java.getResource(path) ?: error("Can't find any resource in $path")
    }

    abstract class Deserializer<T> {
        abstract fun deserialize(name: String): T
    }
}