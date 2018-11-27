package de.unihalle.informatik.bigdata.knjigica.parser.json

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

fun JsonReader.expectName(name: String): String {
    val nextName = nextName()
    if (nextName != name) {
        throw IllegalStateException("Expected the name '$name' but was '$nextName' at path $path.")
    }
    return nextName
}

fun <R> JsonReader.nextObject(block: JsonReader.() -> R): R {
    beginObject()
    val result = block()
    endObject()
    return result
}


fun <R> JsonWriter.objectValue(block: JsonWriter.() -> R): R {
    beginObject()
    val result = block()
    endObject()
    return result
}

inline fun <reified T> Moshi.adapter(): JsonAdapter<T> {
    return adapter(T::class.java)
}

inline fun <reified T> Moshi.Builder.add(jsonAdapter: JsonAdapter<T>): Moshi.Builder {
    return add(T::class.java, jsonAdapter)
}
