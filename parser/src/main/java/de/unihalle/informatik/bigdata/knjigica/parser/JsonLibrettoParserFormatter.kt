package de.unihalle.informatik.bigdata.knjigica.parser

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import de.unihalle.informatik.bigdata.knjigica.model.Plot
import de.unihalle.informatik.bigdata.knjigica.parser.architecture.ParserFormatter
import de.unihalle.informatik.bigdata.knjigica.parser.json.*
import okio.BufferedSink
import okio.BufferedSource

object JsonLibrettoParserFormatter : ParserFormatter<BufferedSource, Libretto, BufferedSink> {

    private val moshi: Moshi = Moshi.Builder()
            .add(LocalDateAdapter)
            .add(LocalDateRangeAdapter)
            .add(LanguageRangeAdapter)
            .add<Plot>(PlotAdapter)
            .add(KotlinJsonAdapterFactory())
            .build()

    // TODO Remove indent in production code.
    private val adapter = moshi.adapter<Libretto>().nonNull().indent("  ")

    override suspend fun parse(source: BufferedSource) = adapter.fromJson(source)!!

    override suspend fun format(sink: BufferedSink, data: Libretto) = adapter.toJson(sink, data)
}
