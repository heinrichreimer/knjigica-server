package de.unihalle.informatik.bigdata.knjigica

import de.unihalle.informatik.bigdata.knjigica.parser.JsonLibrettoParserFormatter
import kotlinx.coroutines.runBlocking
import okio.Buffer
import org.junit.Assert
import org.junit.Test

class JsonParserTest {
    private val parser = JsonLibrettoParserFormatter
    private val libretto = Libretti.ENTFUEHRUNG_AUS_DEM_SERAIL

    @Test
    fun testJsonParser() {
        runBlocking {
            // Create a buffer storing the JSON.
            val jsonBuffer = Buffer()

            // Format the libretto and pipe its JSON to the empty buffer.
            parser.format(jsonBuffer, libretto)

            val newLibretto = parser.parse(jsonBuffer)

            Assert.assertEquals(libretto, newLibretto)
        }
    }
}