package de.unihalle.informatik.bigdata.knjigica

import de.unihalle.informatik.bigdata.knjigica.parser.JsonLibrettoParserFormatter
import kotlinx.coroutines.runBlocking
import okio.Buffer
import org.junit.Assert
import org.junit.Test

class TestJsonParser {
    private val parser = JsonLibrettoParserFormatter
    private val libretto = Libretti.ENTFUEHRUNG_AUS_DEM_SERAIL

    @Test
    fun testJsonParser() {
        runBlocking {
            val jsonBuffer = Buffer().also {
                parser.format(libretto).copyTo(it)
            }

            val newLibretto = parser.parse(jsonBuffer)

            Assert.assertEquals(libretto, newLibretto)
        }
    }
}