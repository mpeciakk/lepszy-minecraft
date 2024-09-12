package render

import Renderable

abstract class Renderer<T : Renderable> {
    abstract fun render(t: T)
}