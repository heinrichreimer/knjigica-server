package de.unihalle.informatik.bigdata.knjigica

import de.unihalle.informatik.bigdata.knjigica.parser.JsonLibrettoParserFormatter
import kotlinx.coroutines.runBlocking
import okio.buffer
import okio.sink

object LogJsonParser {
    private val parser = JsonLibrettoParserFormatter
    private val libretto = Libretti.ENTFUEHRUNG_AUS_DEM_SERAIL

    @JvmStatic
    fun main(vararg args: String) {
        runBlocking {
            println(libretto)
            println()

            // Redirect JSON directly to the system output.
            val outputSink = System.out
                    .sink()
                    .buffer()
            // Format JSON.
            parser.format(outputSink, libretto)
            // Flush the buffer to the system output.
            outputSink.flush()
        }
    }
}