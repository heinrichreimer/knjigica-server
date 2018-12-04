package de.unihalle.informatik.bigdata.knjigica.data

data class Role(
        val name: String,
        val description: String? = null,
        val voice: Voice? = null,
        val note: String? = null
) {
    enum class Voice {
        SOPRANO,
        MEZZO_SOPRANO,
        ALTO,
        CONTRALTO,
        TENOR,
        BARITONE,
        BASS
    }
}
