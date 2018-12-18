package de.unihalle.informatik.bigdata.knjigica.parser.architecture

import okio.Sink

interface Formatter<DataType, SinkType : Sink> {
    suspend fun format(sink: SinkType, data: DataType)
}