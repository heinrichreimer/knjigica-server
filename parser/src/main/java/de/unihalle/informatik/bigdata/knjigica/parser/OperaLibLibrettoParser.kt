package de.unihalle.informatik.bigdata.knjigica.parser

import de.unihalle.informatik.bigdata.knjigica.data.*
import de.unihalle.informatik.bigdata.knjigica.util.languageRange
import okio.BufferedSource
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.TextNode
import java.text.Normalizer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

// TODO Remove debug logging after testing.
class OperaLibLibrettoParser(
        private val baseUri: String = ""
) : Parser<BufferedSource, Libretto> {

    companion object {
        val LANGUAGE_SCRIPT_REGEX = Regex("menuEUrid\\(\\W*?'([a-z]{3})'\\W*?\\)\\W*?;")
        val PREMIERE_DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMMM uuuu")!!
        val ILLEGAL_ROLE_VOICE_CHARACTERS_REGEX = Regex("[-,. ]")
        val SURROUNDING_BRACES_REGEX = Regex("[\\[(](.*)[])]")
        val CONJUNCTION_REGEX = Regex("\\W*\\b(and|e|et|und)\\b\\W*", RegexOption.IGNORE_CASE)
    }

    override suspend fun parse(source: BufferedSource): Libretto {
        val document = Jsoup.parse(source.inputStream(), Charsets.UTF_8.name(), baseUri)
        val locale = parseLocale(document)

        val title = document
                .select("h2")
                .first()
                .text()
                .also { println("title: $it") }

        val opera = document
                .select("div#ps-tabella")

        val meta = opera
                .select("div.rid_opera")

        val ridInfos = document
                .select("p[class=\"rid_info\"]")

        val subtitle = ridInfos[0]
                .wholeText()
                .lineSequence()
                .firstOrNull()
                .also { println("subtitle: $it") }

        val ridInfoBolds = ridInfos[1]
                .select(" > b")


        val hasCombinedAuthor = ridInfos[1]
                .wholeText()
                .lineSequence()
                .firstOrNull()
                .also { println("authorLine: $it") }
        // TODO "Das Rheingold" from "Richard Wagner" / "Mefistofele" from "Arrigo Boito" has both music and text author combined.
        val textAuthor = ridInfoBolds[0]
                .text()
        println("textAuthor: $textAuthor")
        val musicAuthor = ridInfoBolds[1]
                .text()
        println("musicAuthor: $musicAuthor")
        val authors = setOf(
                Author(textAuthor, scope = Author.Scope.TEXT),
                Author(musicAuthor, scope = Author.Scope.MUSIC)
        )

        val (premiereDateString, premiereLocation) = ridInfoBolds[2].text()
                .split(',')
                .map(String::trim)
        println("premiereDateString: $premiereDateString")
        println("premiereLocation: $premiereLocation")
        val premiereDate = try {
            PREMIERE_DATE_FORMATTER.withLocale(locale)
                    .parse(premiereDateString)
                    .let { LocalDate.from(it) }
        } catch (e: DateTimeParseException) {
            null
        }
        val premiere = if (premiereDate != null) {
            Premiere(
                    premiereDate,
                    premiereLocation
            )
        } else null
        println("premiereDate: ${premiereDate?.let(DateTimeFormatter.ISO_DATE::format)}")

        val rolesTable = meta
                .select("table")
                .first()
        val roles: Set<Role> = rolesTable
                .select("tr")
                .filter { row ->
                    row.select("td.vtit").isEmpty()
                }
                .mapTo(mutableSetOf()) { row ->
                    val descriptionColumn = row
                            .select("td.vdes > p")
                            .first()
                    val roleName = descriptionColumn
                            .select("b")
                            .first()
                            .text()
                    val roleDescription = descriptionColumn
                            .text()
                            .takeUnless { it == roleName }

                    val roleVoiceString = row
                            .select("td.vreg > p > i")
                            .first()
                            .text()
                            .let { Normalizer.normalize(it, Normalizer.Form.NFD) }
                            .toLowerCase()
                            .trim()
                            .replace(ILLEGAL_ROLE_VOICE_CHARACTERS_REGEX, "")
                    val roleVoice = when (roleVoiceString) {
                        "sconosciuto", "unknown" -> null // unknown
                        "soprano", "sopran", "сопрано" -> Role.Voice.SOPRANO
                        "mezzosoprano", "mezzosopran", "меццосопрано" -> Role.Voice.MEZZO_SOPRANO
                        "alto", "alt", "altro", "альт" -> Role.Voice.ALTO
                        "contralto", "contraalto", "контральто" -> Role.Voice.CONTRALTO
                        "tenor", "tenore", "тенор" -> Role.Voice.TENOR
                        "baritone", "bariton", "baritono", "баритон" -> Role.Voice.BARITONE
                        "bass", "basso", "basse", "бас" -> Role.Voice.BASS
                        else -> {
                            if (roleVoiceString.isNotBlank()) {
                                System.err.println("Could not resolve voice type '$roleVoiceString'.")
                            }
                            null
                        }
                    }
                    val role = Role(roleName, roleDescription, roleVoice)
                    println("role: $role")

                    role
                }

        val sideRoles = ridInfos[2]
                .textNodes()
                .joinToString(transform = TextNode::text)
                .split("\n\n")
                .first()
                .let {
                    // Drop heading (separated with `:`).
                    val colonIndex = it.indexOf(':')
                    it.drop(colonIndex + 1)
                }
                .split(';')
                .let {
                    // If side characters are not separated by semicolons or dots,
                    // try splitting by colons.
                    if (it.size == 1) it.first().split(',')
                    else it
                }
                .map { it.removeSuffix(".") }
                .map(String::trim)
                .filter(String::isNotBlank)
                .map {
                    val role = Role(it)
                    println("sideRole: $role")
                    role
                }

        val setting = ridInfos[2]
                .select("i")
                .text()
                .trim()
                .takeIf(String::isNotBlank)
        println("setting: $setting")

        val plotDivs = opera.select("> div")
                .dropWhile { !it.hasClass("rid_p_frg") }

        var allTitle: String? = null
        val allPlotDivs = plotDivs
                .mapNotNull { element ->
                    when {
                        element.hasClass("rid_alltit") -> {
                            allTitle = element.text()
                            null
                        }
                        element.hasClass("rid_alltxt") -> allTitle to element.text()
                        else -> null
                    }
                }
                .groupBy { it.first }

        println(allPlotDivs)

        var roleName: Set<String> = emptySet()
        val plot = plotDivs
                .mapNotNull { plotDiv ->
                    when {
                        plotDiv.hasClass("rid_a") -> {
                            plotDiv.text().let { Plot.Section(it, Plot.Section.Level.ACT) }
                        }
                        plotDiv.hasClass("rid_s") -> {
                            plotDiv.text().let { Plot.Section(it, Plot.Section.Level.SCENE) }
                        }
                        plotDiv.hasClass("rid_p_num") -> {
                            plotDiv.text()
                                    .replace(SURROUNDING_BRACES_REGEX) { it.groups[1]?.value ?: "" }
                                    .takeIf(String::isNotBlank)
                                    ?.let { Plot.Section(it, Plot.Section.Level.NUMBER) }
                        }
                        plotDiv.hasClass("rid_voce") -> {
                            roleName = plotDiv.text().split(CONJUNCTION_REGEX).toSet()
                            null
                        }
                        plotDiv.classNames().any { it.startsWith("rid_testo") } -> {
                            val names = roleName
                                    .also {
                                        if (it.isEmpty()) {
                                            System.err.println("No role name specified.")
                                        }
                                    }
                            val text = plotDiv
                                    .select("> p:not(.rid_indt)")
                                    .eachText()
                                    .joinToString("\n")
                            val instruction = plotDiv
                                    .select("> p.rid_indt")
                                    .eachText()
                                    .map { it.replace(SURROUNDING_BRACES_REGEX) { it.groups[1]?.value ?: "" } }
                                    .filter(String::isNotBlank)
                                    .takeIf { it.isNotEmpty() }
                                    ?.joinToString("\n")

                            Plot.Text(names, text, instruction)
                        }
                        plotDiv.hasClass("rid_p_ind") -> {
                            plotDiv.text()
                                    .takeIf(String::isNotBlank)
                                    ?.let { Plot.Instruction(it) }
                        }
                        plotDiv.hasClass("rid_p_scn") -> {
                            plotDiv.text()
                                    .takeIf(String::isNotBlank)
                                    ?.let { Plot.Instruction(it) }
                        }
                        else -> null
                    }
                }

        plot.forEach { println("plot: $it") }

        val libretto = Libretto(
                title,
                subtitle,
                locale.languageRange,
                authors,
                emptyList(),
                premiere,
                roles + sideRoles,
                plot
        )
        println(libretto)
        return libretto
    }

    private fun parseLocale(document: Document): Locale {
        return document
                .head()
                .select("script[type=\"text/javascript\"]")
                .last()
                .html()
                .lineSequence()
                .map(String::trim)
                .firstOrNull { it matches LANGUAGE_SCRIPT_REGEX }
                ?.replace(LANGUAGE_SCRIPT_REGEX) { result ->
                    result.groups[1]?.value ?: ""
                }
                ?.takeIf(String::isNotBlank)
                .let { languageString ->
                    when (languageString) {
                        "fra" -> Locale.FRENCH
                        "deu" -> Locale.GERMAN
                        "eng" -> Locale.ENGLISH
                        "rus" -> Locale.Builder().setLanguage("ru").setScript("Cyrl").build();
                        else -> Locale.ITALIAN
                    }
                }
                .also { println("language: ${it.languageRange.range}") }
    }
}