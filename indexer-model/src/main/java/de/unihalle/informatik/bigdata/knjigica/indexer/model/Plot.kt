package de.unihalle.informatik.bigdata.knjigica.indexer.model

data class Plot(
        val operaTitle: String,
        /**
         * Map containing the section name for all known levels.
         */
        val section: Map<String, String>,
        val roleName: String?,
        val text: String?,
        val instruction: String?
)
