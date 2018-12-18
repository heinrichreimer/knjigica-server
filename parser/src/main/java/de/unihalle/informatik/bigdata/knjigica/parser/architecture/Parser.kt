package de.unihalle.informatik.bigdata.knjigica.parser.architecture

import okio.Source

interface Parser<SourceType : Source, DataType> {
    suspend fun parse(source: SourceType): DataType
}