package de.unihalle.informatik.bigdata.knjigica.indexer

import de.unihalle.informatik.bigdata.knjigica.indexer.util.rest.restHighLevelClientOf
import org.apache.http.HttpHost

object Configuration {
    private const val PORT = 9200
    private val HOST = HttpHost("localhost", PORT, "http")
    val CLIENT = restHighLevelClientOf(HOST)


    const val LIBRETTO_INDEX = "libretti"
    const val LIBRETTO_TYPE = "libretto"
}