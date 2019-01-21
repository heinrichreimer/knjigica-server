package de.unihalle.informatik.bigdata.knjigica.indexer.model

import java.util.*

data class Annotation(
        val id: UUID,
        val operaId: UUID,
        val title: String,
        val text: String
)