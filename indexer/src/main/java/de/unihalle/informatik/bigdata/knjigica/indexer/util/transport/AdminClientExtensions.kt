package de.unihalle.informatik.bigdata.knjigica.indexer.util.transport

import org.elasticsearch.client.AdminClient
import org.elasticsearch.client.ClusterAdminClient
import org.elasticsearch.client.IndicesAdminClient

fun <R> AdminClient.cluster(block: ClusterAdminClient.() -> R) = cluster().run(block)
fun <R> AdminClient.indices(block: IndicesAdminClient.() -> R) = indices().run(block)
