package de.unihalle.informatik.bigdata.knjigica.model

sealed class Plot {

    data class Section(
            val section: String,
            val level: de.unihalle.informatik.bigdata.knjigica.model.Plot.Section.Level // TODO can we omit this?
    ) : de.unihalle.informatik.bigdata.knjigica.model.Plot() {
        enum class Level {
            ACT, NUMBER, SCENE
        }
    }

    data class Text(
            val roleName: Set<String>,
            val text: String,
            val instruction: String? = null
    ) : de.unihalle.informatik.bigdata.knjigica.model.Plot() {

        constructor(
                roleName: String,
                text: String,
                instruction: String? = null
        ) : this(setOf(roleName), text, instruction)
    }

    data class Instruction(
            val instruction: String
    ) : de.unihalle.informatik.bigdata.knjigica.model.Plot()
}
