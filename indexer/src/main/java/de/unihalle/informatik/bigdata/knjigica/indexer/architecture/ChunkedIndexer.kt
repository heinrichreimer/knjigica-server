package de.unihalle.informatik.bigdata.knjigica.indexer.architecture

import org.elasticsearch.client.RestHighLevelClient

private const val DEFAULT_CHUNK_SIZE = 100

class ChunkedIndexer<T>(
        private val indexer: Indexer<T>,
        private val chunkSize: Int = DEFAULT_CHUNK_SIZE
) : Indexer<T> {

    override suspend fun index(client: RestHighLevelClient, items: Iterable<T>) {
        items.chunked(chunkSize)
                .forEach { chunk ->
                    indexer.index(client, chunk)
                }
    }
}

fun <T> Indexer<T>.chunked(chunkSize: Int = DEFAULT_CHUNK_SIZE): Indexer<T> = ChunkedIndexer(this)