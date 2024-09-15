package ui.value

class FloatValue(default: Float = 0f) {
    val arr = floatArrayOf(default)

    fun get(): Float {
        return arr[0]
    }
}