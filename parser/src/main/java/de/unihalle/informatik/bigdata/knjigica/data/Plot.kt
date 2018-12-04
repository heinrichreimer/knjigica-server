package de.unihalle.informatik.bigdata.knjigica.data

import com.squareup.moshi.Json

sealed class Plot {

    data class Section(
            val section: String,
            val level: Level // TODO can we omit this?
    ) : Plot() {
        enum class Level {
            ACT, NUMBER, SCENE
        }
    }

    data class Text(
            @Json(name = "role_name")
            val roleName: String,
            val text: String,
            val instruction: String? = null
    ) : Plot()

    data class Instruction(
            val instruction: String
    ) : Plot()
}
