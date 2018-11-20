package de.unihalle.informatik.bigdata.knjigica.parser

import de.unihalle.informatik.bigdata.knjigica.data.Libretto
import org.jsoup.Jsoup
import java.io.File


object OperaLibLibrettoParser : Parser<File, Libretto> {
    override suspend fun parse(source: File): Libretto {
        val document = Jsoup.parse(source, Charsets.UTF_8.name())

        val languageScript = document.select("head > script[type=\"text/javascript\"]").last().text()
        val language

        val title = document.select("h2")[0].text()

        val ridInfos = document.select("p[class=\"rid_info\"]")
        val subtitle = ridInfos[0].text()

        val ridInfoBolds = ridInfos[1].select(" > b")
        val textAuthor = ridInfoBolds[0].text()
        val musicAuthor = ridInfoBolds[1].text()

        val author = document.select("p[class=\"rid_info\"] > b")
        val note = document.select
        val premiere.date = document.select
        val premiere.place = document.select
        val roles.name = document.select
        val roles.description = document.select
        val roles.voice = document.select
        val plot.Section.section = document.select
        val plot.Section.level = document.select
        val plot.Text.role = document.select
        val plot.Text.text = document.select
        val plot.Instruction.instruction = document.select
    }
}