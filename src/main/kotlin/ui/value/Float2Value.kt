package ui.value

open class Float2Value(defaultX: Float = 0f, defaultY: Float = 0f) {
    val arr = floatArrayOf(defaultX, defaultY)

    fun getX(): Float {
        return arr[0]
    }

    fun getY(): Float {
        return arr[1]
    }
}