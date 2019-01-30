package de.unihalle.informatik.bigdata.knjigica.indexer.util

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest
import java.util.*

enum class MappingType(internal val jsonName: String) {
    OBJECT("object"),

    TEXT("text"),
    KEYWORD("keyword"),

    LONG("long"),
    INTEGER("integer"),
    SHORT("short"),
    BYTE("byte"),
    DOUBLE("double"),
    FLOAT("float"),
    HALF_FLOAT("half_float"),
    SCALED_FLOAT("scaled_float"),

    DATE("date"),

    BOOLEAN("boolean"),

    BINARY("binary"),

    INTEGER_RANGE("integer_range"),
    FLOAT_RANGE("float_range"),
    LONG_RANGE("long_range"),
    DOUBLE_RANGE("double_range"),
    DATE_RANGE("date_range"),

    GEO_POINT("geo_point"),
    GEO_SHAPE("geo_shape"),

    IP("ip"),

    COMPLETION("completion"),

    TOKEN_COUNT("token_count"),

    MURMUR3("murmur3"),

    ANNOTATED_TEXT("annotated-text"),

    JOIN("join"),

    ALIAS("alias")
}

class MappingsBuilder internal constructor() {

    internal val map = mutableMapOf<String, Map<String, Any>>()

    infix fun String.map(type: MappingType) {
        checkFieldName(this)

        map[this] = mapOf(
                "type" to type.jsonName
        )
    }

    operator fun String.invoke(block: MappingsBuilder.() -> Unit) = map(block)

    infix fun String.map(block: MappingsBuilder.() -> Unit) {
        checkFieldName(this)

        val builder = MappingsBuilder()
        builder.block()

        val rootKey = this
        map += builder.map
                .mapKeys { (key, _) -> "$rootKey.$key" }
    }

    private fun checkFieldName(name: String) =
            check(name != "type") { "Field name 'type' is reserved." }
}

fun PutMappingRequest.source(block: MappingsBuilder.() -> Unit): PutMappingRequest {
    val builder = MappingsBuilder()
    builder.block()


    val message = HashMap<String, Any>()
    message["type"] = "text"
    val properties = builder.map
    val jsonMap = mapOf("properties" to properties)

    return source(jsonMap)
}

