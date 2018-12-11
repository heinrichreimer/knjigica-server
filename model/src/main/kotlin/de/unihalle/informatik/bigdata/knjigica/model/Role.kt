package de.unihalle.informatik.bigdata.knjigica.model

data class Role(
        val name: String,
        val description: String? = null,
        val voice: de.unihalle.informatik.bigdata.knjigica.model.Role.Voice? = null,
        val note: String? = null
) {
    enum class Voice {
        SOPRANO,
        MEZZO_SOPRANO,
        ALTO,
        CONTRALTO,
        TENOR,
        BARITONE,
        BASS,
        CHOIR
    }
}
