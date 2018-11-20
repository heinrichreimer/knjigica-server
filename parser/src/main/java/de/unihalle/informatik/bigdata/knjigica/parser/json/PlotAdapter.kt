package de.unihalle.informatik.bigdata.knjigica.parser.json

import com.squareup.moshi.*
import de.unihalle.informatik.bigdata.knjigica.data.Plot


object PlotAdapter {

    private const val FIELD_TYPE = "type"
    private const val FIELD_DATA = "data"

    private const val TYPE_SECTION = "section"
    private const val TYPE_TEXT = "text"
    private const val TYPE_INSTRUCTION = "instruction"

    @FromJson
    fun fromJson(map: Map<String, Any>): Plot {
        val moshi = Moshi.Builder().build()
        val data = map[FIELD_DATA] ?: throw JsonDataException("Missing plot.")
        return when (map[FIELD_TYPE]) {
            TYPE_SECTION -> data as Plot.Section
            TYPE_TEXT -> data as Plot.Text
            TYPE_INSTRUCTION -> data as Plot.Instruction
            else -> throw JsonDataException("Plot has unknown type.")
        }
    }

    @ToJson
    fun toJson(plot: Plot): Map<String, Any> {
        val moshi = Moshi.Builder().build()
        val type = when (plot) {
            is Plot.Section -> TYPE_SECTION
            is Plot.Text -> TYPE_TEXT
            is Plot.Instruction -> TYPE_INSTRUCTION
        }
        return mapOf(
                FIELD_TYPE to type,
                FIELD_DATA to plot
        )
    }
}