package de.unihalle.informatik.bigdata.knjigica.indexer.model

import de.unihalle.informatik.bigdata.knjigica.model.Role
import java.util.*

data class Role(
        val id: UUID,
        val operaId: UUID,
        val name: String,
        val description: String?,
        val voice: Role.Voice?,
        val note: String?
) {
}
