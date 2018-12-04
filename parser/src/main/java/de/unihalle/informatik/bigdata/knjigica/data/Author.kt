package de.unihalle.informatik.bigdata.knjigica.data

import com.squareup.moshi.Json
import java.time.LocalDate

data class Author (
        val name: String,
        @Json(name = "full_name")
        val fullName: String = name,
        val lifetime: ClosedRange<LocalDate>? = null,
        val scopes: Set<Scope>
) {

    constructor(
            name: String,
            fullName: String = name,
            lifetime: ClosedRange<LocalDate>? = null,
            scope: Scope
    ) : this(name, fullName, lifetime, setOf(scope))

    enum class Scope {
        MUSIC,
        TEXT
    }
}