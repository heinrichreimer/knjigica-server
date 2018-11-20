package de.unihalle.informatik.bigdata.knjigica.parser

interface Parser<SourceType, DataType> {
    suspend fun parse(source: SourceType): DataType
}