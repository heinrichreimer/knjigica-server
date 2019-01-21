package de.unihalle.informatik.bigdata.knjigica.indexer

import com.heinrichreimer.elasticsearch.kotlin.dsl.coroutines.rest.bulkAsync
import com.heinrichreimer.elasticsearch.kotlin.dsl.coroutines.rest.createAsync
import com.heinrichreimer.elasticsearch.kotlin.dsl.coroutines.rest.existsAsync
import com.heinrichreimer.elasticsearch.kotlin.dsl.index
import com.heinrichreimer.elasticsearch.kotlin.dsl.rest.indices
import de.unihalle.informatik.bigdata.knjigica.indexer.architecture.Indexer
import de.unihalle.informatik.bigdata.knjigica.indexer.formatter.JsonFormatters
import de.unihalle.informatik.bigdata.knjigica.indexer.formatter.toJsonBytes
import de.unihalle.informatik.bigdata.knjigica.indexer.model.*
import de.unihalle.informatik.bigdata.knjigica.indexer.model.Annotation
import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import org.elasticsearch.action.bulk.BulkItemResponse
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.client.IndicesClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.bytes.BytesReference
import org.elasticsearch.common.xcontent.XContentType

object LibrettoIndexer : Indexer<Libretto> {

    override suspend fun index(client: RestHighLevelClient, items: Iterable<Libretto>) {

        // First create the index if it does not exist yet.
        client.indices {
            createIfNotExistsAsync(IndexConfiguration.Annotation.index)
            createIfNotExistsAsync(IndexConfiguration.Author.index)
            createIfNotExistsAsync(IndexConfiguration.Opera.index)
            createIfNotExistsAsync(IndexConfiguration.Plot.index)
            createIfNotExistsAsync(IndexConfiguration.Role.index)
        }

        // Flatten the Libretto and get it's components.
        val (
                annotations,
                authors,
                operas,
                plot,
                roles
        ) = items
                .asSequence()
                .flatten()

        client.bulkAsync(annotations.asIterable()) { index(it) }
        client.bulkAsync(authors.asIterable()) { index(it) }
        client.bulkAsync(operas.asIterable()) { index(it) }
        client.bulkAsync(plot.asIterable()) { index(it) }
        client.bulkAsync(roles.asIterable()) { index(it) }
    }

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

    private suspend fun <T> RestHighLevelClient.bulkAsync(items: Iterable<T>, action: suspend BulkRequest.(T) -> Unit) {
        val response = bulkAsync {
            items.forEach { action(it) }
        }
        val allResponses: List<BulkItemResponse> = response.items.toList()
        if (response.hasFailures()) {
            val failedResponses = allResponses.filter { it.isFailed }
            System.err.println("Bulk index response contains failures:")
            failedResponses.forEach {
                System.err.println("${it.id}: ${it.failureMessage}")
            }
        }
        val successfulResponses = allResponses.filter { !it.isFailed }
        println("Indexed ${successfulResponses.size} items." +
                if (allResponses.size != successfulResponses.size) " (${allResponses.size} in total)" else "")
    }

    private fun BulkRequest.index(configuration: IndexConfiguration, jsonSource: BytesReference): BulkRequest {
        return index {
            index(configuration.index)
            type(configuration.type)
            source(jsonSource, XContentType.JSON)
        }
    }

    private suspend inline fun BulkRequest.index(libretto: Annotation) =
            index(IndexConfiguration.Annotation, libretto.toJsonBytes())

    private suspend inline fun BulkRequest.index(libretto: Author) =
            index(IndexConfiguration.Author, libretto.toJsonBytes())

    private suspend inline fun BulkRequest.index(libretto: Opera) =
            index(IndexConfiguration.Opera, libretto.toJsonBytes())

    private suspend inline fun BulkRequest.index(libretto: Plot) =
            index(IndexConfiguration.Plot, libretto.toJsonBytes())

    private suspend inline fun BulkRequest.index(libretto: Role) =
            index(IndexConfiguration.Role, libretto.toJsonBytes())

    private suspend fun Annotation.toJsonBytes() = JsonFormatters.annotationFormatter.toJsonBytes(this)
    private suspend fun Author.toJsonBytes() = JsonFormatters.authorFormatter.toJsonBytes(this)
    private suspend fun Opera.toJsonBytes() = JsonFormatters.operaFormatter.toJsonBytes(this)
    private suspend fun Plot.toJsonBytes() = JsonFormatters.plotFormatter.toJsonBytes(this)
    private suspend fun Role.toJsonBytes() = JsonFormatters.roleFormatter.toJsonBytes(this)
}