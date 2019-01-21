package de.unihalle.informatik.bigdata.knjigica.parser.architecture

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
        inputFiles.forEach { input ->
            //FIXME Use parallelForEach again.
            println("Opening source buffer for file '${input.path}'.")
            val source = input
                    .source()
                    .buffer()
            println("Parsing.")
            val data = parser.parse(source)

            val output = File(outputDirectory, namingStrategy(data, input))
            println("Opening sink buffer for file '${output.path}'.")
            val sink = output
                    .sink()
                    .buffer()
            println("Formatting.")
            formatter.format(sink, data)
            println("Done.")
            output.useLines { it.forEach { println(it) } }
            println("Printed.")
            // FIXME This cuts off the JSON at some point.
        }
    }
}