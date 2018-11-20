package de.unihalle.informatik.bigdata.knjigica.parser

import de.unihalle.informatik.bigdata.knjigica.data.Libretto
import de.unihalle.informatik.bigdata.knjigica.util.languageRange
import org.jsoup.Jsoup
import java.io.File
import java.util.*


object OperaLibLibrettoParser : Parser<File, Libretto> {
    override suspend fun parse(source: File): Libretto {
        val document = Jsoup.parse(source, Charsets.UTF_8.name())

        val languageScript = document.head()
                .select("script[type=\"text/javascript\"]")
                .last()
                .html()
        val languageScriptRegex: Regex = Regex("menuEUrid\\( ?'([a-z]{3})' ?\\) ?;")
        val languageString = languageScript.lineSequence()
                .map(String::trim)
                .firstOrNull { it matches languageScriptRegex }
                ?.replace(languageScriptRegex) { result ->
                    result.groups[1]?.value ?: ""
                }
                ?.takeIf(String::isNotBlank)
        val language = when (languageString) {
            "fra" -> Locale.FRENCH.languageRange
            "deu" -> Locale.GERMAN.languageRange
            "eng" -> Locale.ENGLISH.languageRange
            "rus" -> Locale.LanguageRange("ru")
            else -> Locale.ITALIAN.languageRange
        }
        println("language: ${language.range}")

        val title = document.select("h2")[0].text()
        println("title: $title")

        val ridInfos = document.select("p[class=\"rid_info\"]")
        val subtitle = ridInfos[0].text().lineSequence().firstOrNull()
        println("subtitle: $subtitle")

        val ridInfoBolds = ridInfos[1].select(" > b")
        val textAuthor = ridInfoBolds[0].text()
        println("textAuthor: $textAuthor")
        val musicAuthor = ridInfoBolds[1].text()
        println("musicAuthor: $musicAuthor")

        TODO()
    }
}