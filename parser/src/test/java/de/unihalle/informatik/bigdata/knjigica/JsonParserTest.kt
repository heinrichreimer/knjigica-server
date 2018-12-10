package de.unihalle.informatik.bigdata.knjigica

import de.unihalle.informatik.bigdata.knjigica.parser.JsonLibrettoParserFormatter
import kotlinx.coroutines.runBlocking
import okio.Buffer
import org.junit.Before
import org.junit.Test

class JsonParserTest {
    private val parser = JsonLibrettoParserFormatter
    private val libretto = Libretti.ENTFUEHRUNG_AUS_DEM_SERAIL

    // Buffer storing the JSON.
    private val jsonBuffer = Buffer()

    @Before
    fun clearBuffer() {
        jsonBuffer.clear()
    }

    @Test
    fun `Test that libretto JSON is not empty`() {
        // Buffer is empty initially
        jsonBuffer.exhausted() `should equal` true

        runBlocking {
            // Format the libretto and pipe its JSON to the empty buffer.
            parser.format(jsonBuffer, libretto)
        }

        // Buffer should be filled now
        jsonBuffer.exhausted() `should equal` false
    }

    @Test
    fun `Test that JSON formatting is decodable`() {
        runBlocking {
            // Format the libretto and pipe its JSON to the empty buffer.
            parser.format(jsonBuffer, libretto)
            val newLibretto = parser.parse(jsonBuffer)

            newLibretto `should equal` libretto
        }
    }
}