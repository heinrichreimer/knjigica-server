package de.unihalle.informatik.bigdata.knjigica.indexer.model

import de.unihalle.informatik.bigdata.knjigica.model.Role

data class Role(
        val operaTitle: String,
        val name: String,
        val description: String?,
        val voice: Role.Voice?,
        val note: String?
) {
}
