
package de.unihalle.informatik.bigdata.knjigica.indexer

import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.index
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.bulk
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.toBytesReference
import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import de.unihalle.informatik.bigdata.knjigica.parser.JsonLibrettoParserFormatter
import okio.Buffer
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType

object Indexer {
    private const val CHUNK_SIZE = 70

    suspend fun index(client: RestHighLevelClient, libretti: Iterable<Libretto>) {
        libretti.chunked(CHUNK_SIZE)
                .forEach { chunk ->
                    client.bulk {
                        chunk.forEach { libretto ->
                            index(libretto)
                        }
                    }.let { response ->
                        if (response.hasFailures()) {
                            System.err.println("Bulk index response contains failures:")
                            response.items.filter { it.isFailed }
                                    .forEach {
                                        System.err.println("${it.id}: ${it.failureMessage}")
                                    }
                        }
                        println("Indexed ${response.items.size} items.")
                    }
                }
    }

    private suspend fun BulkRequest.index(libretto: Libretto) =
            index {
                index(Configuration.LIBRETTO_INDEX)
                type(Configuration.LIBRETTO_TYPE)
                source(libretto.toJsonBytes(), XContentType.JSON)
            }

    private suspend fun Libretto.toJsonBytes() =
            Buffer().also { JsonLibrettoParserFormatter.format(it, this) }.readByteArray().toBytesReference()
}