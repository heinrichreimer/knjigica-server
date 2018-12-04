package de.unihalle.informatik.bigdata.knjigica.data

data class Role(
        var name: String,
        var description: String? = null,
        var voice: Voice? = null,
        var note: String? = null
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
