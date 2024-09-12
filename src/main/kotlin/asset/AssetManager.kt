package asset

object AssetManager {
    private val assets = mutableMapOf<Class<*>, MutableMap<String, Any>>()

    fun register(type: Class<*>, name: String, asset: Any) {
        if (!assets.containsKey(type)) {
            assets[type] = mutableMapOf()
        }

        assets[type]!![name] = asset
    }

    operator fun <T> get(type: Class<T>, name: String): T {
        if (assets.containsKey(type)) {
            val assetsOfType = assets[type]!!

            if (assetsOfType.containsKey(name)) {
                return assetsOfType[name] as T
            }
        }

        throw IllegalStateException("Can't find asset of type $type with name $name")
    }

    operator fun get(type: Class<*>): MutableMap<String, Any> {
        if (assets.containsKey(type)) {
            return assets[type]!!
        }

        throw IllegalStateException("Can't find assets of type $type")
    }
}