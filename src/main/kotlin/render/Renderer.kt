package render

abstract class Renderer<T : Renderable> {
    abstract fun render(t: T)
}