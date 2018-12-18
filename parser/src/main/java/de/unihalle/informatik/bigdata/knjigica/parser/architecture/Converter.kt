package de.unihalle.informatik.bigdata.knjigica.parser.architecture

import de.unihalle.informatik.bigdata.knjigica.parser.util.parallelForEach
import okio.*
import java.io.File

abstract class Converter<T> {
    abstract val parser: Parser<BufferedSource, T>
    abstract val formatter: Formatter<T, BufferedSink>

    open val namingStrategy: (data: T, inputFile: File) -> String = { _, file -> file.nameWithoutExtension }

    suspend fun convert(inputDirectory: File, outputDirectory: File) {
        val inputFiles = inputDirectory
                .listFiles()
                .asIterable()
        inputFiles.parallelForEach { input ->
            val source = input
                    .source()
                    .buffer()
            val data = parser.parse(source)

            val output = File(outputDirectory, namingStrategy(data, input))
            val sink = output
                    .sink()
                    .buffer()
            formatter.format(sink, data)
        }
    }
}