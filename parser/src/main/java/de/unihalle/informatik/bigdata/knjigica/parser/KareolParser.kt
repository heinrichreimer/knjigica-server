package de.unihalle.informatik.bigdata.knjigica.parser

import de.unihalle.informatik.bigdata.knjigica.data.Libretto
import de.unihalle.informatik.bigdata.knjigica.util.languageRange
import org.jsoup.Jsoup
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.Locale

object KareolParser : Parser<File, Libretto> {
        override suspend fun parse(source: File): Libretto{
                val document = Jsoup.parse(source, Charsets.UTF_8.name());

                //language?
                val title = document.select("p > b > u > font")[0].text()
                val subtitle = document.select("table > tr > td > p > strong > font")[0].text()
                val number = document.select("table > tr > td > p > strong > font")[].text()
                val instruction = document.select("table > tr > td > p > font > em")
                val scene = document.select("table > tr > td > p > font > strong")[].text()
                val text = document.select("table > tr > td > p > font")
                val role_name_temp = document.select("table > tr > td > p > strong")

        }
}
