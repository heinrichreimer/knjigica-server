package de.unihalle.informatik.bigdata.knjigica.indexer.model

import de.unihalle.informatik.bigdata.knjigica.model.Libretto as ModelLibretto
import de.unihalle.informatik.bigdata.knjigica.model.Plot as ModelPlot

fun Sequence<ModelLibretto>.flatten(): LibrettoHolder {
    return map { libretto ->
        val operaId = libretto.title
        LibrettoHolder(
                libretto = libretto.librettoSequence(),
                annotations = libretto.annotationSequence(operaId),
                authors = libretto.authorSequence(operaId),
                operas = libretto.operaSequence(),
                plot = libretto.plotSequence(operaId),
                roles = libretto.roleSequence(operaId)
        )
    }.reduce { accumulator, holder ->
        LibrettoHolder(
                libretto = accumulator.libretto + holder.libretto,
                annotations = accumulator.annotations + holder.annotations,
                authors = accumulator.authors + holder.authors,
                operas = accumulator.operas + holder.operas,
                plot = accumulator.plot + holder.plot,
                roles = accumulator.roles + holder.roles
        )
    }
}

private fun ModelLibretto.librettoSequence(): Sequence<Libretto> {
    return sequenceOf(
            Libretto(
                    title,
                    subtitle,
                    language,
                    authorSequence(title).toSet(),
                    annotationSequence(title).toSet(),
                    premiere,
                    roleSequence(title).toSet(),
                    plotSequence(title).toList()
            )
    )
}


private fun ModelLibretto.operaSequence(): Sequence<Opera> {
    return sequenceOf(
            Opera(
                    title,
                    subtitle,
                    language,
                    premiere
            )
    )
}

private fun ModelLibretto.annotationSequence(operaTitle: String): Sequence<Annotation> {
    return annotations
            .asSequence()
            .map { annotation ->
                Annotation(
                        operaTitle,
                        annotation.title,
                        annotation.text
                )
            }
}

private fun ModelLibretto.authorSequence(operaTitle: String): Sequence<Author> {
    return authors
            .asSequence()
            .flatMap { author ->
                author
                        .scopes
                        .asSequence()
                        .map { scope ->
                            Author(
                                    operaTitle,
                                    author.name,
                                    author.fullName,
                                    author.lifetime,
                                    scope
                            )
                        }
            }
}

private fun ModelLibretto.plotSequence(operaTitle: String): Sequence<Plot> {
    return plot
            .asSequence()
            .zipLastSection()
            .flatMap { (sections, plot) ->
                when (plot) {
                    is ModelPlot.Text -> {
                        plot
                                .roleName
                                .asSequence()
                                .map { roleName ->
                                    Plot(
                                            operaTitle,
                                            sections.mapKeys { (level, _) ->
                                                level.name
                                            },
                                            roleName,
                                            plot.text,
                                            plot.instruction
                                    )
                                }
                    }
                    is ModelPlot.Instruction -> {
                        sequenceOf(
                                Plot(
                                        operaTitle,
                                        sections.mapKeys { (level, _) ->
                                            level.name
                                        },
                                        null,
                                        null,
                                        plot.instruction
                                )
                        )
                    }
                    else -> emptySequence()
                }
            }
}

private fun ModelLibretto.roleSequence(operaTitle: String): Sequence<Role> {
    return roles
            .asSequence()
            .map { role ->
                Role(
                        operaTitle,
                        role.name,
                        role.description,
                        role.voice,
                        role.note
                )
            }
}

private fun Sequence<ModelPlot>.zipLastSection() =
        ZipLastSectionSequence(this)

private class ZipLastSectionSequence(
        private val sequence: Sequence<ModelPlot>
) : Sequence<Pair<Map<ModelPlot.Section.Level, String>, ModelPlot>> {

    override fun iterator() =
            object : Iterator<Pair<Map<ModelPlot.Section.Level, String>, ModelPlot>> {
                val sections = mutableMapOf<ModelPlot.Section.Level, String>()

                val iterator = sequence.iterator()
                override fun next(): Pair<Map<ModelPlot.Section.Level, String>, ModelPlot> {
                    val next = iterator.next()
                    if (next is ModelPlot.Section) {
                        sections[next.level] = next.section
                    }
                    return sections to next
                }

                override fun hasNext() = iterator.hasNext()
            }
}