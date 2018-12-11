package de.unihalle.informatik.bigdata.knjigica.indexer

import de.unihalle.informatik.bigdata.knjigica.parser.OperaLibLibrettoParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okio.buffer
import okio.source
import org.elasticsearch.client.RestHighLevelClient
import java.io.File

object OperaLibIndexer {
    private val CORPUS = File("corpus/crawl/html/opera_lib_libretto/")
    private val PARSER = OperaLibLibrettoParser(CORPUS.absolutePath)

    fun index(client: RestHighLevelClient) {
        val libretti = CORPUS.listFiles()
                .asSequence()
                .map { file ->
                    val source = file.source().buffer()
                    println("Parsing OperaLib libretto at '${file.absolutePath}'.")

                    runBlocking(Dispatchers.IO) {
                        PARSER.parse(source)
                    }
                }
                .chunked(70) { it.asSequence() }
                .forEach {
                    Indexer.index(client, it)
                }
    }
}