package de.unihalle.informatik.bigdata.knjigica.data

import java.util.*

data class Libretto(
        val title: String,
        val subtitle: String?,
        val language: Locale.LanguageRange,
        val authors: List<Author>,
        val notes: List<String>,
        val premiere: Premiere?,
        val roles: Set<Role>,
        val plot: List<Plot>
)
