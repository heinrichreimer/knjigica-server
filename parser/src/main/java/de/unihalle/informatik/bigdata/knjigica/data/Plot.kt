package de.unihalle.informatik.bigdata.knjigica.data

import com.squareup.moshi.Json

sealed class Plot {

    data class Section(
            var section: String,    //name
            var level: Level        //Abwägung, ob Unterteilung notwendig
    ) : Plot() {
        enum class Level {
            ACT, NUMBER, SCENE
        }
    }

    data class Text(
            @Json(name = "role_name") var roleName: String,
            var text: String,
            var instruction: String
    ) : Plot()

    data class Instruction(
            var instruction: String
    ) : Plot()
}
