package de.unihalle.informatik.bigdata.knjigica.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*


object UUIDAdapter {

    @FromJson
    fun fromJson(uuid: String): UUID = UUID.fromString(uuid)

    @ToJson
    fun toJson(uuid: UUID): String = uuid.toString()

}