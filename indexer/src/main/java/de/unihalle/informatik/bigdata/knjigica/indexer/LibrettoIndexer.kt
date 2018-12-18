package de.unihalle.informatik.bigdata.knjigica.indexer

import de.unihalle.informatik.bigdata.knjigica.indexer.architecture.Indexer
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.rest.bulkAsync
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.rest.createAsync
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.rest.existsAsync
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.index
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.indices
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.toBytesReference
import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import de.unihalle.informatik.bigdata.knjigica.parser.JsonLibrettoParserFormatter
import okio.Buffer
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.client.IndicesClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.bytes.BytesReference
import org.elasticsearch.common.xcontent.XContentType

object LibrettoIndexer : Indexer<Libretto> {

    private suspend fun IndicesClient.createIfNotExistsAsync(index: String) {
        val exists = existsAsync {
            indices(index)
        }
        if (!exists) {
            createAsync {
                index(index)
            }
        }
    }

    override suspend fun index(client: RestHighLevelClient, items: Iterable<Libretto>) {

        // First create the index if it does not exist yet.
        client.indices {
            createIfNotExistsAsync(Configuration.INDEX)
        }

        val response = client.bulkAsync {
            items.forEach { libretto ->
                index(libretto)
            }
        }
        if (response.hasFailures()) {
            System.err.println("Bulk index response contains failures:")
            response.items
                    .filter { it.isFailed }
                    .forEach {
                        System.err.println("${it.id}: ${it.failureMessage}")
                    }
        }
        println("Indexed ${response.items.size} items.")
    }

    private suspend fun BulkRequest.index(libretto: Libretto): BulkRequest {
        return index {
            index(Configuration.INDEX)
            type(Configuration.LIBRETTO_TYPE)
            source(libretto.toJsonBytes(), XContentType.JSON)
        }
    }

    private suspend fun Libretto.toJsonBytes(): BytesReference {
        return Buffer()
                .also { JsonLibrettoParserFormatter.format(it, this) }
                .readByteArray()
                .toBytesReference()
    }
}