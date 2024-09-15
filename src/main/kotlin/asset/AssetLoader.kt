package asset

import shader.Shader

class AssetLoader : AssetLoaderBase() {

    fun load() {
        queueAsset("default", Shader::class.java)
        queueAsset("instanced_test", Shader::class.java)
        queueAsset("chunk_buffer", Shader::class.java)
        queueAsset("depth", Shader::class.java)
        queueAsset("cobblestone", Texture::class.java)
//        queueAll("/textures", Texture::class.java)
    }
}