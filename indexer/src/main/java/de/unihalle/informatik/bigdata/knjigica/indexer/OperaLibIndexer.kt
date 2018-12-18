package de.unihalle.informatik.bigdata.knjigica.indexer

import de.unihalle.informatik.bigdata.knjigica.indexer.architecture.chunked
import de.unihalle.informatik.bigdata.knjigica.indexer.util.parallelMap
import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import de.unihalle.informatik.bigdata.knjigica.parser.OperaLibLibrettoParser
import okio.buffer
import okio.source
import org.elasticsearch.client.RestHighLevelClient
import java.io.File

object OperaLibIndexer {
    private val CORPUS = File("corpus/crawl/html/opera_lib/")
    private val PARSER = OperaLibLibrettoParser(CORPUS.absolutePath)

    suspend fun index(client: RestHighLevelClient) {
        CORPUS.listFiles()
                .asIterable()
                .parallelMap(::toLibretto)
                .let { Indexer.index(client, it) }
    }

    private suspend fun toLibretto(file: File): Libretto {
        println("Parsing OperaLib libretto at '$file.absolutePath'.")
        return PARSER.parse(file.source().buffer())
    }
}