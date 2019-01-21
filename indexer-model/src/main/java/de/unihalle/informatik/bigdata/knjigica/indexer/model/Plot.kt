package de.unihalle.informatik.bigdata.knjigica.indexer.model

import java.util.*

data class Plot(
        val id: UUID,
        val operaId: UUID,
        /**
         * Map containing the section name for all known levels.
         */
        val section: Map<String, String>,
        val roleName: String?,
        val text: String?,
        val instruction: String?
)
