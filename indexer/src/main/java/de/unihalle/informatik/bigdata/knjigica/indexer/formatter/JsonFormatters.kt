package de.unihalle.informatik.bigdata.knjigica.indexer.formatter

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.unihalle.informatik.bigdata.knjigica.indexer.model.*
import de.unihalle.informatik.bigdata.knjigica.indexer.model.Annotation
import de.unihalle.informatik.bigdata.knjigica.json.LanguageRangeAdapter
import de.unihalle.informatik.bigdata.knjigica.json.LocalDateAdapter
import de.unihalle.informatik.bigdata.knjigica.json.LocalDateRangeAdapter
import de.unihalle.informatik.bigdata.knjigica.json.UUIDAdapter
import de.unihalle.informatik.bigdata.knjigica.parser.architecture.Formatter
import okio.BufferedSink

object JsonFormatters {
    private val moshi: Moshi = Moshi.Builder()
            .add(LocalDateAdapter)
            .add(LocalDateRangeAdapter)
            .add(LanguageRangeAdapter)
            .add(UUIDAdapter)
            .add(KotlinJsonAdapterFactory())
            .build()

    val annotationFormatter: Formatter<Annotation, BufferedSink> = moshi.parserFormatter()
    val authorFormatter: Formatter<Author, BufferedSink> = moshi.parserFormatter()
    val operaFormatter: Formatter<Opera, BufferedSink> = moshi.parserFormatter()
    val plotFormatter: Formatter<Plot, BufferedSink> = moshi.parserFormatter()
    val roleFormatter: Formatter<Role, BufferedSink> = moshi.parserFormatter()
}