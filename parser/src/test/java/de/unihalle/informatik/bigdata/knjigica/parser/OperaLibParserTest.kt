package de.unihalle.informatik.bigdata.knjigica.parser

import de.unihalle.informatik.bigdata.knjigica.model.Author
import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import de.unihalle.informatik.bigdata.knjigica.model.Role
import de.unihalle.informatik.bigdata.knjigica.parser.util.`should equal`
import de.unihalle.informatik.bigdata.knjigica.parser.util.getResourceSource
import de.unihalle.informatik.bigdata.knjigica.parser.util.languageRange
import kotlinx.coroutines.runBlocking
import okio.buffer
import org.junit.Before
import org.junit.Test
import java.util.*

class OperaLibParserTest {

    companion object {
        private const val LES_ABENCERAGES_LIBRETTO_PATH = "operalib_sample_les_abencerages.html"
        private val PARSER = OperaLibLibrettoParser(LES_ABENCERAGES_LIBRETTO_PATH)

        private val FRENCH = Locale.FRENCH.languageRange
    }

    lateinit var parsedLibretto: Libretto

    @Before
    fun parse() = runBlocking {
        // Create an input buffer storing the HTML file.
        val source = getResourceSource(LES_ABENCERAGES_LIBRETTO_PATH)
                .buffer()

        parsedLibretto = PARSER.parse(source)
    }

    @Test
    fun `Test that title should be Les Abencerages`() =
            parsedLibretto.title `should equal` "Les Abencérages"

    @Test
    fun `Test that language should be French`() =
            parsedLibretto.language `should equal` FRENCH

    @Test
    fun `Test that subtitle should be opera en trois actes`() =
            parsedLibretto.subtitle `should equal` "Opéra en trois actes."

    @Test
    fun `Test that author should be Etienne de Jouy`() =
            parsedLibretto.authors.find { Author.Scope.TEXT in it.scopes }
                    ?.fullName `should equal` "Victor-Joseph ÉTIENNE DE JOUY"

    @Test
    fun `Test that Alemar should be bass`() =
            parsedLibretto.roles.find { it.name == "ALÉMAR" }
                    ?.voice `should equal` Role.Voice.BASS
}