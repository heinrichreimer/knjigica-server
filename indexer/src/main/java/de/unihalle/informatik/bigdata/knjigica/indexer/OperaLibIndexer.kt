package de.unihalle.informatik.bigdata.knjigica.indexer

import de.unihalle.informatik.bigdata.knjigica.indexer.chunked.chunked
import de.unihalle.informatik.bigdata.knjigica.indexer.util.parallelMap
import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import de.unihalle.informatik.bigdata.knjigica.parser.JsonLibrettoParserFormatter
import de.unihalle.informatik.bigdata.knjigica.parser.architecture.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okio.BufferedSource
import okio.buffer
import okio.source
import org.elasticsearch.client.RestHighLevelClient
import java.io.File

object OperaLibIndexer {
    private val OPERA_LIB_HTML_CORPUS = File("corpus/crawl/json/opera_lib/")
    private val PARSER: Parser<BufferedSource, Libretto> = JsonLibrettoParserFormatter

    @JvmStatic
    fun main(args: Array<String>) = runBlocking(Dispatchers.IO) {
        Configuration.CLIENT.use { client ->
            index(client)
        }
    }

    suspend fun index(client: RestHighLevelClient) {
        OPERA_LIB_HTML_CORPUS
                .listFiles()
                .asIterable()
                .parallelMap { parseLibretto(it) }
                .let { libretti ->
                    LibrettoIndexer
                            .chunked(70)
                            .index(client, libretti)
                }
    }

    private suspend fun parseLibretto(file: File): Libretto {
        println("Parsing OperaLib libretto at '${file.absolutePath}'.")
        return PARSER.parse(file.source().buffer())
    }
}