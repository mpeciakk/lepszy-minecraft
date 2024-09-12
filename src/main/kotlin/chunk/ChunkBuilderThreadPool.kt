package chunk

import render.mesh.MeshBuilder
import java.util.Collections
import java.util.concurrent.Executors
import java.util.concurrent.Future

class ChunkBuilderThreadPool {


    private val threadPool = Executors.newFixedThreadPool(POOL_SIZE)
    private val builderPool = Collections.synchronizedList(List(POOL_SIZE) { MeshBuilder() })
    private val builderPoolMap = Collections.synchronizedList(List(POOL_SIZE) { true })

    private val chunkToBuilder = Collections.synchronizedMap(mutableMapOf<Chunk, Int>())

    // waits for the builder to free up
    fun getBuilder(chunk: Chunk): MeshBuilder {
        val id = (Thread.currentThread().id % POOL_SIZE).toInt()

        while (!builderPoolMap[id]) {}

        builderPoolMap[id] = false
        chunkToBuilder[chunk] = id

        return builderPool[id]
    }

    fun getBuilderForChunk(chunk: Chunk): MeshBuilder {
        return builderPool[chunkToBuilder[chunk]!!]
    }

    fun freeBuilderForChunk(chunk: Chunk) {
        val builder = chunkToBuilder[chunk]

        builderPool[builder!!].vertices.clear()
        builderPool[builder].indices.clear()

        builderPoolMap[builder] = true
        chunkToBuilder[chunk] = -1
    }

    fun submit(task: Runnable): Future<*> = threadPool.submit(task)

    companion object {
        const val POOL_SIZE = 2
    }
}