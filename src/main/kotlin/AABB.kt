import org.joml.Vector3f

data class AABB(
    var min: Vector3f,
    var max: Vector3f
) {
    // Constructor for more explicit bounding box definition
    constructor(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float) : this(
        Vector3f(minX, minY, minZ), Vector3f(maxX, maxY, maxZ)
    )

    // Center of the AABB
    val center: Vector3f
        get() = Vector3f(
            (min.x + max.x) * 0.5f,
            (min.y + max.y) * 0.5f,
            (min.z + max.z) * 0.5f
        )

    // Size of the AABB (width, height, depth)
    val size: Vector3f
        get() = Vector3f(
            max.x - min.x,
            max.y - min.y,
            max.z - min.z
        )

    // Returns true if the two AABBs intersect
    fun intersects(other: AABB): Boolean {
        return (min.x <= other.max.x && max.x >= other.min.x) &&
                (min.y <= other.max.y && max.y >= other.min.y) &&
                (min.z <= other.max.z && max.z >= other.min.z)
    }

    // Checks if the current AABB contains another AABB
    fun contains(other: AABB): Boolean {
        return (min.x <= other.min.x && max.x >= other.max.x) &&
                (min.y <= other.min.y && max.y >= other.max.y) &&
                (min.z <= other.min.z && max.z >= other.max.z)
    }
}