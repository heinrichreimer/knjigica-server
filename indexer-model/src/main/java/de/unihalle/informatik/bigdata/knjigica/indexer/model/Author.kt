package de.unihalle.informatik.bigdata.knjigica.indexer.model

import de.unihalle.informatik.bigdata.knjigica.model.Author
import java.time.LocalDate
import java.util.*

data class Author(
        val id: UUID,
        val operaId: UUID,
        val name: String,
        val fullName: String = name,
        val lifetime: ClosedRange<LocalDate>?,
        val scope: Author.Scope
)