package asset

import org.lwjgl.opengl.GL11
import java.nio.ByteBuffer

class Texture(val width: Int, val height: Int, buffer: ByteBuffer) {
    val id = GL11.glGenTextures()

    init {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_RGBA,
            width,
            height,
            0,
            GL11.GL_RGBA,
            GL11.GL_UNSIGNED_BYTE,
            buffer
        )

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
    }
}