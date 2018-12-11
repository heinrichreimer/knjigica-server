package de.unihalle.informatik.bigdata.knjigica.parser.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.unihalle.informatik.bigdata.knjigica.model.Plot

object PlotAdapter : JsonAdapter<Plot>() {

    private val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    private const val FIELD_NAME_TYPE = "type"
    private const val FIELD_NAME_DATA = "data"

    enum class PlotType {
        SECTION, TEXT, INSTRUCTION
    }

    override fun fromJson(reader: JsonReader): Plot? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }
        return reader.nextObject {
            reader.expectName(FIELD_NAME_TYPE)
            val type = PlotType.valueOf(reader.nextString())

            reader.expectName(FIELD_NAME_DATA)
            when (type) {
                PlotType.SECTION -> moshi.adapter<Plot.Section>()
                PlotType.TEXT -> moshi.adapter<Plot.Text>()
                PlotType.INSTRUCTION -> moshi.adapter<Plot.Instruction>()
            }.fromJson(reader)
        }
    }

    override fun toJson(writer: JsonWriter, plot: Plot?) {
        if (plot == null) {
            writer.nullValue()
            return
        }

        writer.objectValue {
            writer.name(FIELD_NAME_TYPE)
            writer.value(
                    when (plot) {
                        is Plot.Section -> PlotType.SECTION
                        is Plot.Text -> PlotType.TEXT
                        is Plot.Instruction -> PlotType.INSTRUCTION
                    }.name
            )

            writer.name(FIELD_NAME_DATA)
            when (plot) {
                is Plot.Section -> moshi.adapter<Plot.Section>().toJson(writer, plot)
                is Plot.Text -> moshi.adapter<Plot.Text>().toJson(writer, plot)
                is Plot.Instruction -> moshi.adapter<Plot.Instruction>().toJson(writer, plot)
            }
        }
    }
}