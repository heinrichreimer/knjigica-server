package de.unihalle.informatik.bigdata.knjigica.parser

import de.unihalle.informatik.bigdata.knjigica.model.*
import de.unihalle.informatik.bigdata.knjigica.model.Annotation
import de.unihalle.informatik.bigdata.knjigica.parser.architecture.Parser
import de.unihalle.informatik.bigdata.knjigica.parser.util.languageRange
import okio.BufferedSource
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.text.Normalizer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class OperaLibLibrettoParser(
        private val baseUri: String = ""
) : Parser<BufferedSource, Libretto> {

    companion object {
        private val LANGUAGE_SCRIPT_REGEX = Regex("menuEUrid\\(\\W*?'([a-z]{3})'\\W*?\\)\\W*?;")
        private val PREMIERE_DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMMM uuuu")!!
        private val ILLEGAL_ROLE_VOICE_CHARACTERS_REGEX = Regex("[-,. ]")
        private val SURROUNDING_BRACES_REGEX = Regex("[\\[(](.*)[])]")
        private val CONJUNCTION_REGEX = Regex("\\W*\\b(and|e|et|und)\\b\\W*", RegexOption.IGNORE_CASE)
        private val TEXT_AUTHOR_REGEX = Regex("\\b(libretto|text|текст|livret)\\b", RegexOption.IGNORE_CASE)
        private val MUSIC_AUTHOR_REGEX = Regex("\\b(musica|musik|music|musique|музыка)\\b", RegexOption.IGNORE_CASE)

        private val UNKNOWN_VOICE_REGEX = Regex("unknown|inconnu|unbekannt|неизвестныи|sconosciuto", RegexOption.IGNORE_CASE)
        private val OTHER_VOICE_REGEX = Regex("other|autre|andere|другои|altro", RegexOption.IGNORE_CASE)
        private val SOPRANO_VOICE_REGEX = Regex("soprano?|сопрано", RegexOption.IGNORE_CASE)
        private val MEZZO_SOPRANO_VOICE_REGEX = Regex("mezzosoprano?|меццосопрано", RegexOption.IGNORE_CASE)
        private val ALTO_VOICE_REGEX = Regex("alto?|альт", RegexOption.IGNORE_CASE)
        private val CONTRALTO_VOICE_REGEX = Regex("contra?alt(r?o)?|контральто", RegexOption.IGNORE_CASE)
        private val TENOR_VOICE_REGEX = Regex("tenore?|тенор", RegexOption.IGNORE_CASE)
        private val BARITONE_VOICE_REGEX = Regex("bar[iy]ton[eo]?|баритон", RegexOption.IGNORE_CASE)
        private val BASSO_VOICE_REGEX = Regex("bass[eo]?|бас", RegexOption.IGNORE_CASE)

        private val UNWRAP_FIRST_GROUP_TRANSFORMATION: (MatchResult) -> String = { it.groups[1]?.value ?: "" }
        private val COMBINING_DIACRITICAL_MARKS_REGEX = Regex("\\p{InCombiningDiacriticalMarks}")
        private val SIDE_ROLE_DELIMITER_REGEX = Regex("[.;]")
        private val SIDE_ROLE_DELIMITER_FALLBACK_REGEX = Regex("[,]")
        private val AUTHOR_DELIMITER_REGEX = Regex("[,]")
    }

    override suspend fun parse(source: BufferedSource): Libretto {
        val document = Jsoup.parse(
                source.inputStream(),
                Charsets.UTF_8.name(),
                baseUri
        )
        val locale = parseLocale(document)
        val title = parseTitle(document)
        val subtitle = parseSubtitle(document)
        val authors = parseAuthors(document)
        val premiere = parsePremiere(document)
        val roles = parseRoles(document) + parseSideRoles(document)
        val annotations = parseAnnotations(document)
        val plot = parsePlot(document)
        return Libretto(
                title = title,
                subtitle = subtitle,
                language = locale.languageRange,
                authors = authors,
                annotations = annotations,
                premiere = premiere,
                roles = roles,
                plot = plot
        )
    }

    private fun parseAnnotations(document: Document): Set<Annotation> {
        var annotationTitle: String? = null
        return document
                .select("div#ps-tabella > div")
                .dropWhile { !it.hasClass("rid_p_frg") }
                .mapNotNull { element ->
                    when {
                        element.hasClass("rid_alltit") -> {
                            annotationTitle = element.text()
                            null
                        }
                        element.hasClass("rid_alltxt") -> {
                            val title = annotationTitle ?: ""
                            if (title.isBlank()) {
                                System.err.println("No annotation title specified.")
                            }
                            val text: String? = element
                                    .text()
                                    .takeIf(String::isNotBlank)
                            if (text != null) {
                                Annotation(title, text)
                            } else null
                        }
                        else -> null
                    }
                }
                .groupBy(Annotation::title)
                .mapTo(mutableSetOf()) { group ->
                    Annotation(
                            title = group.key,
                            text = group.value
                                    .joinToString(
                                            separator = "\n",
                                            transform = Annotation::text
                                    )
                    )
                }
    }

    private fun parseSetting(document: Document): Plot.Instruction? {
        return document
                .select("p[class=\"rid_info\"]")[2]
                .select("i")
                .text()
                .trim()
                .takeIf(String::isNotBlank)
                ?.let { Plot.Instruction(it) }
    }

    private fun parsePremiere(document: Document): Premiere? {
        val premiereIndex = 1 + if (hasCombinedAuthor(document)) 0 else 1
        val (premiereDateString, premiereLocation) = document
                .select("p[class=\"rid_info\"]")[1]
                .select(" > b")[premiereIndex]
                .text()
                .split(',')
                .map(String::trim)
        val premiereDate = try {
            PREMIERE_DATE_FORMATTER.withLocale(parseLocale(document))
                    .parse(premiereDateString)
                    .let { LocalDate.from(it) }
        } catch (_: DateTimeParseException) {
            null
        }
        return premiereDate?.let { Premiere(it, premiereLocation) }
    }

    private fun hasCombinedAuthor(document: Document): Boolean {
        return document
                .select("p[class=\"rid_info\"]")[1]
                .wholeText()
                .lineSequence()
                .firstOrNull()
                ?.let {
                    MUSIC_AUTHOR_REGEX.containsMatchIn(it) && TEXT_AUTHOR_REGEX.containsMatchIn(it)
                }
                ?: false
    }

    private fun parseAuthors(document: Document): Set<Author> {

        fun String.getCoreAuthorName(): String {
            return splitToSequence(' ')
                    .filter { word -> word.none(Char::isLowerCase) }
                    .joinToString(separator = " ")
                    .takeIf(String::isNotBlank)
                    ?: this
        }

        val potentialAuthorElements = document
                .select("p[class=\"rid_info\"]")[1]
                .select(" > b")

        val authorNames = potentialAuthorElements[0]
                .text()
                .split(AUTHOR_DELIMITER_REGEX)
        val authors = authorNames
                .mapTo(mutableSetOf()) {
                    Author(
                            name = it.getCoreAuthorName(),
                            fullName = it,
                            scopes = setOf(Author.Scope.TEXT)
                    )
                }

        if (hasCombinedAuthor(document)) {
            // Don't parse music authors separately. Instead just add the music scope to each author.
            return authors
                    .mapTo(mutableSetOf()) { author ->
                        author.copy(scopes = author.scopes + Author.Scope.MUSIC)
                    }
        }

        val musicAuthorNames = potentialAuthorElements[1]
                .text()
                .split(AUTHOR_DELIMITER_REGEX)
        val musicAuthors = musicAuthorNames
                .mapTo(mutableSetOf()) {
                    Author(
                            name = it.getCoreAuthorName(),
                            fullName = it,
                            scope = Author.Scope.MUSIC
                    )
                }

        return authors + musicAuthors
    }

    private fun parseRoles(document: Document): Set<Role> {
        return document
                .selectFirst("div#ps-tabella > div.rid_opera > table")
                .select("tr")
                .filter { row ->
                    row.selectFirst("td.vtit") == null
                }
                .mapTo(mutableSetOf(), ::parseRole)
    }

    private fun parseRole(row: Element): Role {
        val name = row
                .selectFirst("td.vdes > p > b")
                .text()
        val description = row
                .selectFirst("td.vdes > p")
                .text()
                .takeUnless { it == name }

        val voiceString = row
                .selectFirst("td.vreg > p > i")
                .text()
                .trim()
                .toLowerCase()
                .let { Normalizer.normalize(it, Normalizer.Form.NFD) }
                .replace(COMBINING_DIACRITICAL_MARKS_REGEX, "")
                .replace(ILLEGAL_ROLE_VOICE_CHARACTERS_REGEX, "")
        val voice = when {
            voiceString matches OTHER_VOICE_REGEX -> null // Other voice
            voiceString matches UNKNOWN_VOICE_REGEX -> null // Unknown voice
            voiceString matches SOPRANO_VOICE_REGEX -> Role.Voice.SOPRANO
            voiceString matches MEZZO_SOPRANO_VOICE_REGEX -> Role.Voice.MEZZO_SOPRANO
            voiceString matches ALTO_VOICE_REGEX -> Role.Voice.ALTO
            voiceString matches CONTRALTO_VOICE_REGEX -> Role.Voice.CONTRALTO
            voiceString matches TENOR_VOICE_REGEX -> Role.Voice.TENOR
            voiceString matches BARITONE_VOICE_REGEX -> Role.Voice.BARITONE
            voiceString matches BASSO_VOICE_REGEX -> Role.Voice.BASS
            else -> {
                if (voiceString.isNotBlank()) {
                    System.err.println("Could not resolve voice type '$voiceString'.")
                }
                null
            }
        }
        return Role(name, description, voice)
    }

    private fun parseSideRoles(document: Document): Set<Role> {
        return document
                .select("p[class=\"rid_info\"]")[2]
                .wholeText()
                .split("\n\n")
                .map {
                    // Drop heading (separated with `:`).
                    val colonIndex = it.indexOf(':')
                    it.drop(colonIndex + 1)
                }
                .flatMapTo(mutableSetOf()) { line ->
                    line
                            .split(SIDE_ROLE_DELIMITER_REGEX)
                            .let { list ->
                                // If side characters are not separated by semicolons or dots,
                                // try splitting by colons.
                                if (list.size == 1) {
                                    list
                                            .first()
                                            .split(SIDE_ROLE_DELIMITER_FALLBACK_REGEX)
                                } else list
                            }
                            .asSequence()
                            .map { it.removeSuffix(".") }
                            .map(String::trim)
                            .filter(String::isNotBlank)
                            .map { Role(it) }
                            .toSet()
                }
    }

    private fun parseTitle(document: Document): String = document.selectFirst("h2").text()

    private fun parseSubtitle(document: Document) =
            document.selectFirst("p[class=\"rid_info\"]").wholeText().firstLineOrNull()

    private fun parsePlot(document: Document): List<Plot> {
        var roleName: Set<String> = emptySet()
        return document
                .select("div#ps-tabella > div")
                .dropWhile { !it.hasClass("rid_p_frg") }
                .mapNotNull { plotDiv ->
                    when {
                        plotDiv.hasClass("rid_a") -> parsePlotAct(plotDiv)
                        plotDiv.hasClass("rid_s") -> parsePlotScene(plotDiv)
                        plotDiv.hasClass("rid_p_num") -> parsePlotNumber(plotDiv)
                        plotDiv.hasClass("rid_voce") -> {
                            roleName = parsePlotRoleNames(plotDiv)
                            null
                        }
                        plotDiv.hasClassPrefix("rid_testo") -> parsePlotText(plotDiv, roleName)
                        plotDiv.hasClass("rid_p_ind") -> parsePlotInstruction(plotDiv)
                        plotDiv.hasClass("rid_p_scn") -> parsePlotInstruction(plotDiv)
                        else -> null
                    }
                }
                .let { list ->
                    val setting = parseSetting(document)
                    listOfNotNull(setting) + list
                }
    }

    private fun parsePlotAct(element: Element) =
            element.text().let { Plot.Section(it, Plot.Section.Level.ACT) }

    private fun parsePlotScene(element: Element) =
            element.text().let { Plot.Section(it, Plot.Section.Level.SCENE) }

    private fun parsePlotNumber(element: Element): Plot.Section? {
        return element.text()
                .unwrapBraces()
                .takeIf(String::isNotBlank)
                ?.let { Plot.Section(it, Plot.Section.Level.NUMBER) }
    }

    private fun parsePlotRoleNames(element: Element) = element.text().split(CONJUNCTION_REGEX).toSet()

    private fun parsePlotText(element: Element, names: Set<String>): Plot.Text {
        val text = element
                .select("> p:not(.rid_indt)")
                .eachText()
                .joinToString("\n")
        val instruction = element
                .select("> p.rid_indt")
                .eachText()
                .map { it.unwrapBraces() }
                .filter(String::isNotBlank)
                .takeIf { it.isNotEmpty() }
                ?.joinToString("\n")

        if (names.isEmpty()) {
            val snippet = text
                    .replace('\n', ' ')
                    .toCharArray()
                    .joinToString(separator = "", limit = 50)
            System.err.println("No role name specified for text \"$snippet\".")
        }

        return Plot.Text(names, text, instruction)
    }

    private fun parsePlotInstruction(element: Element): Plot.Instruction? {
        return element.text()
                .takeIf(String::isNotBlank)
                ?.unwrapBraces()
                ?.let { Plot.Instruction(it) }
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
                ?.replace(LANGUAGE_SCRIPT_REGEX, UNWRAP_FIRST_GROUP_TRANSFORMATION)
                ?.takeIf(String::isNotBlank)
                .let(::parseLocale)
    }

    private fun parseLocale(languageCode: String?): Locale {
        return when (languageCode) {
            "fra" -> Locale.FRENCH
            "deu" -> Locale.GERMAN
            "eng" -> Locale.ENGLISH
            "rus" -> Locale.Builder().setLanguage("ru").setScript("Cyrl").build()
            else -> Locale.ITALIAN
        }
    }

    private fun Element.hasClassPrefix(prefix: String) = classNames().any { it.startsWith(prefix) }

    private fun String.firstLineOrNull() = lineSequence().firstOrNull()

    private fun String.unwrapFirstGroup(regex: Regex) = replace(regex, UNWRAP_FIRST_GROUP_TRANSFORMATION)

    private fun String.unwrapBraces() = unwrapFirstGroup(SURROUNDING_BRACES_REGEX)
}