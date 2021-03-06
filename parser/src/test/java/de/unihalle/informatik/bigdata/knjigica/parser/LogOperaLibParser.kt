package de.unihalle.informatik.bigdata.knjigica.parser

import kotlinx.coroutines.runBlocking
import okio.JvmStatic
import okio.buffer
import okio.source
import java.io.File
import kotlin.random.Random


object LogOperaLibParser {
    private const val path = "corpus/crawl/html/opera_lib_libretto/rid.html"
    private val parser = OperaLibLibrettoParser(path)

    @JvmStatic
    fun main(vararg args: String) {
        runBlocking {
            val index = Random.nextInt(0, 433) // Choose a random libretto.
            val source = File(path + if (index > 0) ".$index" else "")
                    .source()
                    .buffer()
            println("Parsing OperaLib libretto #$index.")

            val libretto = parser.parse(source)
            println(libretto)
        }
    }
}