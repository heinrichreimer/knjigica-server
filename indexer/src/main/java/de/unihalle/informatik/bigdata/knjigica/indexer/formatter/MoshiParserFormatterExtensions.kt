package de.unihalle.informatik.bigdata.knjigica.indexer.formatter

import com.squareup.moshi.Moshi
import de.unihalle.informatik.bigdata.knjigica.parser.architecture.ParserFormatter
import okio.BufferedSink
import okio.BufferedSource

inline fun <reified T : Any> Moshi.parserFormatter(): ParserFormatter<BufferedSource, T, BufferedSink> =
        MoshiParserFormatter(this, T::class)