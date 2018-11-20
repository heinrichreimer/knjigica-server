package de.unihalle.informatik.bigdata.knjigica.parser

import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import okio.Source
import java.io.File
import jdk.nashorn.tools.ShellFunctions.input
import org.jsoup.Jsoup



class OperaLibLibrettoParser : LibrettoParser {
    override suspend fun parse(source: Source): Libretto
        val input = File("/corpus/input.html")
        var doc = Jsoup.parse(input, "UTF-8", "http://example.com/")
        title = doc.select("h2")
        subtitle = doc.select
        author = doc.select("<p[class="rid_info] > b")
        note = doc.select
        premiere.date = doc.select
        premiere.place = doc.select
        roles.name = doc.select
        roles.description = doc.select
        roles.voice = doc.select
        plot.Section.section = doc.select
        plot.Section.level = doc.select
        plot.Text.role = doc.select
        plot.Text.text = doc.select
        plot.Instruction.instruction = doc.select
}
}