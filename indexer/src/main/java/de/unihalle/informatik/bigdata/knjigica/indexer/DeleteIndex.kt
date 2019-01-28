package de.unihalle.informatik.bigdata.knjigica.indexer

import com.heinrichreimer.elasticsearch.kotlin.dsl.coroutines.rest.deleteAsync
import com.heinrichreimer.elasticsearch.kotlin.dsl.coroutines.rest.existsAsync
import com.heinrichreimer.elasticsearch.kotlin.dsl.rest.indices
import kotlinx.coroutines.runBlocking
import org.elasticsearch.client.IndicesClient

object DeleteIndex {

    private val KNOWN_INDICES = IndexConfiguration.ALL
            .map(IndexConfiguration::index)

    @JvmStatic
    fun main(vararg args: String) {
        runBlocking {
            var indexInput: String? = args.getOrNull(0)
            while (indexInput == null) {
                print("Type in index to delete: ")
                indexInput = readLine()
            }

            val index: String = when (indexInput) {
                in KNOWN_INDICES -> indexInput
                "all" -> {
                    KNOWN_INDICES.forEach {
                        main(it)
                    }
                    return@runBlocking
                }
                else -> {
                    System.err.print("Unknown index type! Do you want to continue deleting index '$indexInput'? (y/N)")
                    if (readLine()?.toLowerCase() == "y") {
                        indexInput
                    } else {
                        return@runBlocking
                    }
                }
            }

            Configuration.CLIENT.indices {
                deleteIfExistsAsync(index)
            }
            return@runBlocking
        }
    }

    private suspend fun IndicesClient.deleteIfExistsAsync(index: String) {
        val exists = existsAsync {
            indices(index)
        }
        if (exists) {
            deleteAsync {
                indices(index)
            }
        }
    }
}
