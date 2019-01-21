package de.unihalle.informatik.bigdata.knjigica.indexer.formatter

import com.heinrichreimer.elasticsearch.kotlin.dsl.toBytesReference
import de.unihalle.informatik.bigdata.knjigica.parser.architecture.Formatter
import okio.Buffer
import okio.BufferedSink
import org.elasticsearch.common.bytes.BytesReference


suspend fun <T> Formatter<T, BufferedSink>.toJsonBytes(data: T): BytesReference {
    return Buffer()
            .also { format(it, data) }
            .readByteArray()
            .toBytesReference()
}