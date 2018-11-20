package de.unihalle.informatik.bigdata.knjigica.data

import com.squareup.moshi.Json
import java.time.LocalDate

data class Author (
        val name: String,
        @Json(name = "full_name") val fullName: String = name,
        val lifetime: ClosedRange<LocalDate>? = null,
        val scope: Scope
) {
    enum class Scope {
        MUSIC,
        TEXT
    }
}