package de.unihalle.informatik.bigdata.knjigica

import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import java.net.InetAddress

class Test {

    init {
        val settings = Settings.EMPTY // Settings.builder().put("cluster.name", "myClusterName").build()
        val client = PreBuiltTransportClient(settings)
                .apply {
                    addTransportAddress(
                            TransportAddress(
                                    InetAddress.getByName("localhost"),
                                    9300
                            )
                    )
                }

        client.use {
            TODO("Use the client to execute queries.")
        }
    }
}