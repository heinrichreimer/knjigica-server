package de.unihalle.informatik.bigdata.knjigica.parser

import de.unihalle.informatik.bigdata.knjigica.data.Libretto
import okio.Source

class KareolLibrettoParser : Parser<Source, Libretto> {
    override suspend fun parse(source: Source): Libretto {
        TODO()
    }
}