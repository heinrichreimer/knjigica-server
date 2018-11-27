package de.unihalle.informatik.bigdata.knjigica

import de.unihalle.informatik.bigdata.knjigica.parser.JsonLibrettoParserFormatter
import de.unihalle.informatik.bigdata.knjigica.parser.OperaLibLibrettoParser
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder
import org.elasticsearch.transport.client.PreBuiltTransportClient
import java.net.InetAddress
import java.util.*
import kotlin.random.Random

object TestIndexer {

    private val jsonParser = JsonLibrettoParserFormatter
    private val operaLibParser = OperaLibLibrettoParser
    private val path = "corpus/crawl/html/opera_lib_libretto/rid.html"
    private val random = Random.Default

    @JvmStatic
    fun main(args: Array<String>) {
        val settings = Settings.EMPTY // Settings.builder().put("cluster.name", "myClusterName").build()
        val transportClient = PreBuiltTransportClient(settings)
                .apply {
                    addTransportAddress(
                            TransportAddress(
                                    InetAddress.getByName("localhost"),
                                    9300
                            )
                    )
                }

        transportClient.use { client ->
            //            val index = random.nextInt(0, 433)
//            val indexedPath = path + if (index > 0) ".$index" else ""
//            val file = File(indexedPath)
//            println(file.absolutePath)

//            val libretto = runBlocking { operaLibParser.parse(file) }
//            val buffer: Buffer
//            jsonParser.format(libretto)

            val response = client.prepareIndex("twitter", "_doc", "1")
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("user", "kimchy")
                            .field("postDate", Date())
                            .field("message", "trying out Elasticsearch")
                            .endObject()
                    )
                    .get()
        }
    }
}