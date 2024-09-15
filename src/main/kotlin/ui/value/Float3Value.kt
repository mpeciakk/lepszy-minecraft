package ui.value

open class Float3Value(defaultX: Float = 0f, defaultY: Float = 0f, defaultZ: Float = 0f) {
    val arr = floatArrayOf(defaultX, defaultY, defaultZ)

    fun getX(): Float {
        return arr[0]
    }

    fun getY(): Float {
        return arr[1]
    }

    fun getZ(): Float {
        return arr[2]
    }
}