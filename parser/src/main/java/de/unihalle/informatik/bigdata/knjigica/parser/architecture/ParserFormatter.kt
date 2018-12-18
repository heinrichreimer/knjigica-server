package de.unihalle.informatik.bigdata.knjigica.parser.architecture

import okio.Sink
import okio.Source

interface ParserFormatter<SourceType : Source, DataType, SinkType : Sink> :
        Parser<SourceType, DataType>, Formatter<DataType, SinkType>