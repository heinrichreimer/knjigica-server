package de.unihalle.informatik.bigdata.knjigica.parser

interface Formatter<DataType, SinkType> {
    suspend fun format(data: DataType): SinkType
}