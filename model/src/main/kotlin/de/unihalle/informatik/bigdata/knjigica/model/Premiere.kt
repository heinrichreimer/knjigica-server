package de.unihalle.informatik.bigdata.knjigica.model

import java.time.LocalDate

data class Premiere(
        val date: LocalDate,
        val place: String? = null
)
