package de.unihalle.informatik.bigdata.knjigica.indexer

import com.heinrichreimer.elasticsearch.kotlin.dsl.coroutines.rest.bulkAsync
import com.heinrichreimer.elasticsearch.kotlin.dsl.coroutines.rest.createAsync
import com.heinrichreimer.elasticsearch.kotlin.dsl.coroutines.rest.existsAsync
import com.heinrichreimer.elasticsearch.kotlin.dsl.coroutines.rest.putMappingAsync
import com.heinrichreimer.elasticsearch.kotlin.dsl.index
import com.heinrichreimer.elasticsearch.kotlin.dsl.rest.indices
import de.unihalle.informatik.bigdata.knjigica.indexer.architecture.Indexer
import de.unihalle.informatik.bigdata.knjigica.indexer.formatter.JsonFormatters
import de.unihalle.informatik.bigdata.knjigica.indexer.formatter.toJsonBytes
import de.unihalle.informatik.bigdata.knjigica.indexer.model.*
import de.unihalle.informatik.bigdata.knjigica.indexer.model.Annotation
import de.unihalle.informatik.bigdata.knjigica.indexer.util.MappingType
import de.unihalle.informatik.bigdata.knjigica.indexer.util.source
import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import de.unihalle.informatik.bigdata.knjigica.model.Plot.Section
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
            createIfNotExistsAsync(IndexConfiguration.Libretto.index)
            putMappingAsync {
                indices(IndexConfiguration.Libretto.index)
                type(IndexConfiguration.Libretto.type)

                source {
                    "title" map MappingType.TEXT
                    "subtitle" map MappingType.TEXT
                    "language" map MappingType.KEYWORD
                    "authors" nested {
                        "name" map MappingType.TEXT
                        "fullName" map MappingType.TEXT
                        "scope" map MappingType.KEYWORD
                    }
                    "annotations" nested {
                        "title" map MappingType.TEXT
                        "text" map MappingType.TEXT
                    }
                    "premiere" map {
                        "date" map MappingType.DATE
                        "place" map MappingType.KEYWORD
                    }
                    "roles" nested {
                        "name" map MappingType.TEXT
                        "voice" map MappingType.KEYWORD
                    }
                    "plot" nested {
                        "section" map {
                            Section.Level.ACT.name map MappingType.TEXT
                            Section.Level.SCENE.name map MappingType.TEXT
                            Section.Level.NUMBER.name map MappingType.TEXT
                        }
                        "roleName" map MappingType.TEXT
                        "text" map MappingType.TEXT
                        "instruction" map MappingType.TEXT
                    }
                }
            }
            createIfNotExistsAsync(IndexConfiguration.Annotation.index)
            putMappingAsync {
                indices(IndexConfiguration.Annotation.index)
                type(IndexConfiguration.Annotation.type)

                source {
                    "operaTitle" map MappingType.TEXT
                    "title" map MappingType.TEXT
                    "text" map MappingType.TEXT
                }
            }
            createIfNotExistsAsync(IndexConfiguration.Author.index)
            putMappingAsync {
                indices(IndexConfiguration.Author.index)
                type(IndexConfiguration.Author.type)

                source {
                    "operaTitle" map MappingType.TEXT
                    "name" map MappingType.TEXT
                    "fullName" map MappingType.TEXT
                    "lifetime" map MappingType.DATE_RANGE
                    "scope" map MappingType.KEYWORD
                }
            }
            createIfNotExistsAsync(IndexConfiguration.Opera.index)
            putMappingAsync {
                indices(IndexConfiguration.Opera.index)
                type(IndexConfiguration.Opera.type)
                source {
                    "operaTitle" map MappingType.TEXT
                    "subtitle" map MappingType.TEXT
                    "language" map MappingType.KEYWORD
                    "premiere" map {
                        "date" map MappingType.DATE
                        "place" map MappingType.KEYWORD
                    }
                }
            }
            createIfNotExistsAsync(IndexConfiguration.Plot.index)
            putMappingAsync {
                indices(IndexConfiguration.Plot.index)
                type(IndexConfiguration.Plot.type)
                source {
                    "operaTitle" map MappingType.TEXT
                    "section" map {
                        Section.Level.ACT.name map MappingType.TEXT
                        Section.Level.SCENE.name map MappingType.TEXT
                        Section.Level.NUMBER.name map MappingType.TEXT
                    }
                    "roleName" map MappingType.TEXT
                    "text" map MappingType.TEXT
                    "instruction" map MappingType.TEXT
                }
            }
            createIfNotExistsAsync(IndexConfiguration.Role.index)
            putMappingAsync {
                indices(IndexConfiguration.Role.index)
                type(IndexConfiguration.Role.type)
                source {
                    "operaTitle" map MappingType.TEXT
                    "name" map MappingType.TEXT
                    "description" map MappingType.TEXT
                    "voice" map MappingType.KEYWORD
                    "note" map MappingType.TEXT
                }
            }
        }

        // Flatten the Libretto and get it's components.
        val (
                librettos,
                annotations,
                authors,
                operas,
                plot,
                roles
        ) = items
                .asSequence()
                .flatten()

        client.bulkAsync(librettos.asIterable()) { index(it) }
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

    private suspend inline fun BulkRequest.index(libretto: de.unihalle.informatik.bigdata.knjigica.indexer.model.Libretto) =
            index(IndexConfiguration.Libretto, libretto.toJsonBytes())

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

    private suspend fun de.unihalle.informatik.bigdata.knjigica.indexer.model.Libretto.toJsonBytes() = JsonFormatters.librettoFormatter.toJsonBytes(this)
    private suspend fun Annotation.toJsonBytes() = JsonFormatters.annotationFormatter.toJsonBytes(this)
    private suspend fun Author.toJsonBytes() = JsonFormatters.authorFormatter.toJsonBytes(this)
    private suspend fun Opera.toJsonBytes() = JsonFormatters.operaFormatter.toJsonBytes(this)
    private suspend fun Plot.toJsonBytes() = JsonFormatters.plotFormatter.toJsonBytes(this)
    private suspend fun Role.toJsonBytes() = JsonFormatters.roleFormatter.toJsonBytes(this)
}