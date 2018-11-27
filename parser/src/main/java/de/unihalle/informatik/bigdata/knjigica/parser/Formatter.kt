package de.unihalle.informatik.bigdata.knjigica.parser

import okio.Sink

interface Formatter<DataType, SinkType : Sink> {
    suspend fun format(sink: SinkType, data: DataType)
}