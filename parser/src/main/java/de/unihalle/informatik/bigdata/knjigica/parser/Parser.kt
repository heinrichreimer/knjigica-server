package de.unihalle.informatik.bigdata.knjigica.parser

import okio.Source

interface Parser<SourceType : Source, DataType> {
    suspend fun parse(source: SourceType): DataType
}