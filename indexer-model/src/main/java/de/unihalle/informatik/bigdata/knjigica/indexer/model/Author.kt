package de.unihalle.informatik.bigdata.knjigica.indexer.model

import de.unihalle.informatik.bigdata.knjigica.model.Author
import java.time.LocalDate

data class Author(
        val operaTitle: String,
        val name: String,
        val fullName: String = name,
        val lifetime: ClosedRange<LocalDate>?,
        val scope: Author.Scope
)