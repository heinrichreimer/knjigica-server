package de.unihalle.informatik.bigdata.knjigica.indexer.formatter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate

object ElasticsearchDateRangeAdapter {

    @FromJson
    fun fromJson(range: JsonLocalDateRange) = range.gte..range.lte

    @ToJson
    fun toJson(range: ClosedRange<LocalDate>) = JsonLocalDateRange(range.start, range.endInclusive)

    class JsonLocalDateRange internal constructor(
            val gte: LocalDate,
            val lte: LocalDate
    )
}