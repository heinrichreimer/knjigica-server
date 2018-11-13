package de.unihalle.informatik.bigdata.knjigica.parser

import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import okio.Source

interface LibrettoParser {
    suspend fun parse(source: Source): Libretto
}