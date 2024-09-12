package chunk

enum class ChunkState {
    NONE,
    TO_BUILD,
    BUILDING,
    BUILT,
    READY
}