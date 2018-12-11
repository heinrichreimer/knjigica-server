package de.unihalle.informatik.bigdata.knjigica.indexer

import de.unihalle.informatik.bigdata.knjigica.indexer.util.index
import de.unihalle.informatik.bigdata.knjigica.indexer.util.rest.bulk
import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import de.unihalle.informatik.bigdata.knjigica.parser.JsonLibrettoParserFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okio.Buffer
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType

object Indexer {
    fun index(client: RestHighLevelClient, libretti: Sequence<Libretto>) {
        client.bulk {
            libretti.forEach {
                val buffer = Buffer()
                runBlocking(Dispatchers.IO) {
                    JsonLibrettoParserFormatter.format(buffer, it)
                }
                index(Configuration.LIBRETTO_INDEX, Configuration.LIBRETTO_TYPE) {
                    source(buffer.readByteArray(), XContentType.JSON)
                }
            }
        }.run {
            if (hasFailures()) {
                System.err.println("Bulk index response contains failures:")
                items.filter { it.isFailed }
                        .forEach {
                            System.err.println("${it.id}: ${it.failureMessage}")
                        }
            }
            println("items count: ${items.size}")
            println("items: ${items}")
        }
    }
}