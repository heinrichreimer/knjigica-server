package de.unihalle.informatik.bigdata.knjigica.parser

import de.unihalle.informatik.bigdata.knjigica.model.*
import de.unihalle.informatik.bigdata.knjigica.model.Annotation
import de.unihalle.informatik.bigdata.knjigica.parser.util.languageRange
import java.time.LocalDate
import java.time.Month
import java.util.*

object Libretti {
    val ENTFUEHRUNG_AUS_DEM_SERAIL = Libretto(
            title = "Die Entführung aus dem Serail",
            subtitle = "Deutsche Singspiel.",
            language = Locale.GERMAN.languageRange,
            authors = setOf(
                    Author(
                            name = "Wolfgang Amadeus Mozart",
                            fullName = "Joannes Chrysostomus Wolfgangus Theophilus Mozart",
                            lifetime = LocalDate.of(1756, Month.JANUARY, 27)
                                    .rangeTo(LocalDate.of(1791, Month.DECEMBER, 5)),
                            scope = Author.Scope.MUSIC
                    ),
                    Author(
                            name = "Christoph Friedrich Bretzner",
                            lifetime = LocalDate.of(1748, Month.DECEMBER, 10)
                                    .rangeTo(LocalDate.of(1807, Month.AUGUST, 31)),
                            scope = Author.Scope.TEXT
                    ),
                    Author(
                            name = "Johann Gottlieb Stephanie",
                            fullName = "Johann Gottlieb Stephanie d. J.",
                            lifetime = LocalDate.of(1741, Month.FEBRUARY, 19)
                                    .rangeTo(LocalDate.of(1800, Month.JANUARY, 23)),
                            scope = Author.Scope.TEXT
                    )
            ),
            annotations = setOf(
                    Annotation(
                            "Quelle",
                            "Syntetische Fassung herausgegeben von www.operalib.eu."
                    )
            ),
            premiere = Premiere(
                    date = LocalDate.of(1782, Month.JULY, 16),
                    place = "Wien"
            ),
            roles = setOf(
                    Role(
                            name = "Selim",
                            description = "Bassa"
                    ),
                    Role(
                            name = "Konstanze",
                            description = "Geliebte des Belmonte",
                            voice = Role.Voice.SOPRANO
                    ),
                    Role(
                            name = "Blonde",
                            description = "Mädchen der Konstanze",
                            voice = Role.Voice.SOPRANO
                    ),
                    Role(
                            name = "Belmonte",
                            voice = Role.Voice.TENOR
                    ),
                    Role(
                            name = "Pedrillo",
                            description = "Bedienter des Belmonte und Aufseher über das Landhaus des Bassa",
                            voice = Role.Voice.TENOR
                    ),
                    Role(
                            name = "Osmin",
                            description = "Aufseher über das Landhaus des Bassa",
                            voice = Role.Voice.BASS
                    ),
                    Role(
                            name = "Klaas",
                            description = "ein Schiffer"
                    ),
                    Role(
                            name = "Ein Stummer"
                    ),
                    Role(
                            name = "Wache",
                            description = "Chor der Janitscharen"
                    )
            ),
            plot = listOf(
                    Plot.Instruction(
                            instruction = "Die Szene ist auf dem Landgut des Bassa."
                    ),
                    Plot.Section(
                            section = "Erster Aufzug",
                            level = Plot.Section.Level.ACT
                    ),
                    Plot.Section(
                            section = "Erster Auftritt",
                            level = Plot.Section.Level.SCENE
                    ),
                    Plot.Instruction(
                            instruction = "Platz vor dem Palast des Bassa am Ufer des Meeres.\nBelmonte allein."
                    ),
                    Plot.Section(
                            section = "Nr. 1 - Arie",
                            level = Plot.Section.Level.NUMBER
                    ),
                    Plot.Text(
                            roleName = "Belmonte",
                            text = "Hier soll ich dich denn sehen;\nKonstanze! dich mein Glück!\n" +
                                    "Laß Himmel es geschehen!\nGib mir die Ruh zurück.\n\n" +
                                    "Ich duldete der Leiden\nO Liebe! allzuviel!\n" +
                                    "Schenk mir dafür nun Freuden\nUnd bringe mich ans Ziel!"
                    ),
                    Plot.Text(
                            roleName = "Belmonte",
                            text = "Aber wie soll ich in den Palast kommen? Wie sie sehen? Wie sprechen?"
                    )
                    // ~
                    //
                    //Zweiter Auftritt
                    //Belmonte, Osmin.
                    //
                    //[Nr. 2 - Lied und Duett]
                    //
                    //OSMIN
                    //
                    //(mit einer Leiter, welche er an einen Baum vor der Tür des Palastes lehnt, hinaufsteigt und Feigen abnimmt)
                    //
                    //Wer ein Liebchen hat gefunden,
                    //
                    //Die es treu und redlich meint,
                    //
                    //Lohn' es ihr durch tausend Küsse,
                    //
                    //Mach ihr all das Leben süße,
                    //
                    //Sei ihr Tröster, sei ihr Freund.
                    //
                    //Trallalera, trallalera.
            )
    )
}
