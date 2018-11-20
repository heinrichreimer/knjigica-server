package de.unihalle.informatik.bigdata.knjigica

import de.unihalle.informatik.bigdata.knjigica.parser.JsonLibrettoParserFormatter
import kotlinx.coroutines.runBlocking
import okio.Buffer

object LogJsonParser {
    private val parser = JsonLibrettoParserFormatter
    private val libretto = Libretti.ENTFUEHRUNG_AUS_DEM_SERAIL

    @JvmStatic
    fun main(vararg args: String) {
        runBlocking {
            println(libretto)

            val jsonBuffer = Buffer().also {
                parser.format(libretto).copyTo(it)
            }
            val json = jsonBuffer.clone().readString(Charsets.UTF_8)
            println(json)

            val newLibretto = parser.parse(jsonBuffer)
            println(newLibretto)
        }
    }
}