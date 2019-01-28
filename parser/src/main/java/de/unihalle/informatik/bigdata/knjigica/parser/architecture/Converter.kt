package de.unihalle.informatik.bigdata.knjigica.parser.architecture

import de.unihalle.informatik.bigdata.knjigica.parser.util.parallelForEach
import okio.*
import java.io.File
import kotlin.system.measureTimeMillis

abstract class Converter<T> {
    abstract val parser: Parser<BufferedSource, T>
    abstract val formatter: Formatter<T, BufferedSink>

    open val namingStrategy: (data: T, inputFile: File) -> String = { _, file -> file.nameWithoutExtension }

    suspend fun convert(inputDirectory: File, outputDirectory: File) {
        val inputFiles = inputDirectory
                .listFiles()
        val time = measureTimeMillis {
            inputFiles
                    .asIterable()
                    .parallelForEach { input ->
                        val source = input
                                .source()
                                .buffer()
                        println("Parsing from source file '${input.path}'.")
                        val data = parser.parse(source)

                        val output = File(outputDirectory, namingStrategy(data, input))
                        val sink = output
                                .sink()
                                .buffer()
                        println("Formatting to file '${output.path}'.")
                        formatter.format(sink, data)
                        sink.emit()
                    }
        }
        println("Converting ${inputFiles.size} files took $time ms (${time / inputFiles.size} ms/file).")
    }
}