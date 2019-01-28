package de.unihalle.informatik.bigdata.knjigica.parser

import de.unihalle.informatik.bigdata.knjigica.model.Author
import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import de.unihalle.informatik.bigdata.knjigica.parser.architecture.Converter
import de.unihalle.informatik.bigdata.knjigica.parser.architecture.Formatter
import de.unihalle.informatik.bigdata.knjigica.parser.architecture.Parser
import kotlinx.coroutines.runBlocking
import net.gcardone.junidecode.Junidecode
import okio.BufferedSink
import okio.BufferedSource
import java.io.File

object OperaLibConverter : Converter<Libretto>() {
    private val OPERA_LIB_HTML_CORPUS = File("corpus/crawl/html/opera_lib/")
    private val OPERA_LIB_JSON_CORPUS = File("corpus/crawl/json/opera_lib/")

    private val ILLEGAL_FILE_NAME_CHARACTERS = Regex("[\\W_]")

    override val parser: Parser<BufferedSource, Libretto> = OperaLibLibrettoParser(OPERA_LIB_HTML_CORPUS.absolutePath)
    override val formatter: Formatter<Libretto, BufferedSink> = JsonLibrettoParserFormatter
    override val namingStrategy: (data: Libretto, inputFile: File) -> String =
            fun(libretto, _): String {
                fun String.normalizeToSnakeCase(): String {
                    return toLowerCase()
                            .let(Junidecode::unidecode)
                            .replace(ILLEGAL_FILE_NAME_CHARACTERS, " ")
                            .splitToSequence(' ')
                            .filter(String::isNotBlank)
                            .joinToString(separator = "_")
                }

                val author = libretto.authors
                        .find { Author.Scope.TEXT in it.scopes }
                        ?: libretto.authors.first()
                val authorName = author.name.normalizeToSnakeCase()
                val title = libretto.title.normalizeToSnakeCase()

                return "$authorName-$title-opera_lib.json"
            }

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        OPERA_LIB_JSON_CORPUS.mkdirs()
        convert(OPERA_LIB_HTML_CORPUS, OPERA_LIB_JSON_CORPUS)
    }
}