package de.unihalle.informatik.bigdata.knjigica.indexer.chunked

import de.unihalle.informatik.bigdata.knjigica.indexer.architecture.Indexer
import org.elasticsearch.client.RestHighLevelClient

internal class ChunkedIndexer<T>(
        private val indexer: Indexer<T>,
        private val chunkSize: Int = DEFAULT_CHUNK_SIZE
) : Indexer<T> {

    override suspend fun index(client: RestHighLevelClient, items: Iterable<T>) {
        items.chunked(chunkSize)
                .forEach { chunk ->
                    indexer.index(client, chunk)
                }
    }

    companion object {
        internal const val DEFAULT_CHUNK_SIZE = 100
    }
}
