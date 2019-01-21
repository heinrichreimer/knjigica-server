package de.unihalle.informatik.bigdata.knjigica.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

object LanguageRangeAdapter {

    @FromJson
    fun fromJson(range: String): Locale.LanguageRange = Locale.LanguageRange(range)

    @ToJson
    fun toJson(range: Locale.LanguageRange): String = range.range
}