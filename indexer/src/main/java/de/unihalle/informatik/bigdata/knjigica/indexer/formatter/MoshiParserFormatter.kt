package de.unihalle.informatik.bigdata.knjigica.indexer.formatter

import com.squareup.moshi.Moshi
import de.unihalle.informatik.bigdata.knjigica.json.adapter
import de.unihalle.informatik.bigdata.knjigica.parser.architecture.ParserFormatter
import okio.BufferedSink
import okio.BufferedSource
import kotlin.reflect.KClass

class MoshiParserFormatter<T : Any>(
        moshi: Moshi,
        type: KClass<T>
) : ParserFormatter<BufferedSource, T, BufferedSink> {
    private val adapter = moshi.adapter(type).nullSafe().indent("  ")

    override suspend fun parse(source: BufferedSource): T {
        return adapter.fromJson(source)!!
    }

    // TODO Remove JSON indent in production code.
    override suspend fun format(sink: BufferedSink, data: T) =
            adapter.toJson(sink, data)
}
