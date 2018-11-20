package de.unihalle.informatik.bigdata.knjigica.parser

interface ParserFormatter<SourceType, DataType, SinkType> : Parser<SourceType, DataType>, Formatter<DataType, SinkType>