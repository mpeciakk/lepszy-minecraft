package asset

import asset.AssetLoaderBase.Deserializer
import org.lwjgl.BufferUtils
import shader.Shader
import javax.imageio.ImageIO

class AssetLoader : AssetLoaderBase() {
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

    fun load() {
        queueAsset("default", Shader::class.java)
        queueAsset("instanced_test", Shader::class.java)
        queueAll("/textures", Texture::class.java)
    }
}