package de.unihalle.informatik.bigdata.knjigica

import de.unihalle.informatik.bigdata.knjigica.parser.OperaLibLibrettoParser
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.random.Random

object LogOperaLibParser {
    private val parser = OperaLibLibrettoParser
    private val path = "corpus/crawl/html/opera_lib_libretto/rid.html"
    private val random = Random.Default

    @JvmStatic
    fun main(vararg args: String) {
        runBlocking {
            val index = random.nextInt(0, 433)
            val indexedPath = path +
                    if (index > 0) ".$index" else ""
            val file = File(indexedPath)
            println(file.absolutePath)

            parser.parse(file)
        }
    }
}