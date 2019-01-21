package de.unihalle.informatik.bigdata.knjigica.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object LocalDateAdapter {

    private val format: DateTimeFormatter = DateTimeFormatter.ISO_DATE

    @FromJson
    fun fromJson(date: String): LocalDate = LocalDate.from(format.parse(date))

    @ToJson
    fun toJson(date: LocalDate): String = format.format(date)

}