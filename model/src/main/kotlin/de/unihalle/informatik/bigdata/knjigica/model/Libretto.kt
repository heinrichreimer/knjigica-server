package de.unihalle.informatik.bigdata.knjigica.model

import java.util.*

data class Libretto(
        val title: String,
        val subtitle: String? = null,
        val language: Locale.LanguageRange,
        val authors: Set<Author>,
        val annotations: Set<Annotation>,
        val premiere: Premiere? = null,
        val roles: Set<Role>,
        val plot: List<Plot>
)
