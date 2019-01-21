package de.unihalle.informatik.bigdata.knjigica.indexer.model

import de.unihalle.informatik.bigdata.knjigica.model.Libretto
import java.util.*
import java.util.UUID.randomUUID
import de.unihalle.informatik.bigdata.knjigica.model.Plot as ModelPlot

fun Sequence<Libretto>.flatten(): LibrettoHolder {
    return map { libretto ->
        val operaId: UUID = randomUUID()
        LibrettoHolder(
                annotations = libretto.annotationSequence(operaId),
                authors = libretto.authorSequence(operaId),
                operas = libretto.operaSequence(operaId),
                plot = libretto.plotSequence(operaId),
                roles = libretto.roleSequence(operaId)
        )
    }.reduce { accumulator, holder ->
        LibrettoHolder(
                annotations = accumulator.annotations + holder.annotations,
                authors = accumulator.authors + holder.authors,
                operas = accumulator.operas + holder.operas,
                plot = accumulator.plot + holder.plot,
                roles = accumulator.roles + holder.roles
        )
    }
}

private fun Libretto.operaSequence(operaId: UUID): Sequence<Opera> {
    return sequenceOf(
            Opera(
                    operaId,
                    title,
                    subtitle,
                    language,
                    premiere
            )
    )
}

private fun Libretto.annotationSequence(operaId: UUID): Sequence<Annotation> {
    return annotations
            .asSequence()
            .map { annotation ->
                Annotation(
                        randomUUID(),
                        operaId,
                        annotation.title,
                        annotation.text
                )
            }
}

private fun Libretto.authorSequence(operaId: UUID): Sequence<Author> {
    return authors
            .asSequence()
            .flatMap { author ->
                author
                        .scopes
                        .asSequence()
                        .map { scope ->
                            Author(
                                    randomUUID(),
                                    operaId,
                                    author.name,
                                    author.fullName,
                                    author.lifetime,
                                    scope
                            )
                        }
            }
}

private fun Libretto.plotSequence(operaId: UUID): Sequence<Plot> {
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
                                            randomUUID(),
                                            operaId,
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
                                        randomUUID(),
                                        operaId,
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

private fun Libretto.roleSequence(operaId: UUID): Sequence<Role> {
    return roles
            .asSequence()
            .map { role ->
                Role(
                        randomUUID(),
                        operaId,
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