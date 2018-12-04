package de.unihalle.informatik.bigdata.knjigica

import de.unihalle.informatik.bigdata.knjigica.parser.JsonLibrettoParserFormatter
import de.unihalle.informatik.bigdata.knjigica.parser.OperaLibLibrettoParser
import de.unihalle.informatik.bigdata.knjigica.util.extension.bulk
import de.unihalle.informatik.bigdata.knjigica.util.extension.index
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder
import org.elasticsearch.transport.client.PreBuiltTransportClient
import java.net.InetAddress
import java.util.*
import kotlin.random.Random

object TestIndexer {

    private val jsonParser = JsonLibrettoParserFormatter
    private val path = "corpus/crawl/html/opera_lib_libretto/rid.html"
    private val operaLibParser = OperaLibLibrettoParser(path)
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

            val test = client.index {
                setIndex("twitter")
                setType("_doc")
                setId("1")
                setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                )
            }

            client.bulk {
                ad
            }
        }
    }
}