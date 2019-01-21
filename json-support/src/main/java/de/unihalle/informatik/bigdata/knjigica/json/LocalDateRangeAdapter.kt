package de.unihalle.informatik.bigdata.knjigica.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate

object LocalDateRangeAdapter {

    @FromJson
    fun fromJson(range: JsonLocalDateRange) = range.from..range.to

    @ToJson
    fun toJson(range: ClosedRange<LocalDate>) = JsonLocalDateRange(range.start, range.endInclusive)

    class JsonLocalDateRange internal constructor(
            val from: LocalDate,
            val to: LocalDate
    )
}