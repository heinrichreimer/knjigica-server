package de.unihalle.informatik.bigdata.knjigica.data

import java.time.LocalDate

data class Premiere(
        var date: LocalDate,
        var place: String? = null
)
