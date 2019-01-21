package de.unihalle.informatik.bigdata.knjigica.indexer.model

import de.unihalle.informatik.bigdata.knjigica.model.Premiere
import java.util.*

data class Opera(
        val id: UUID,
        val title: String,
        val subtitle: String?,
        val language: Locale.LanguageRange,
        val premiere: Premiere?
)
