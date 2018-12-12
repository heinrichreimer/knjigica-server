@file:Suppress("UNUSED")

package de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.rest

import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.awaitAction
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.exploreAsync
import org.elasticsearch.client.GraphClient
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.protocol.xpack.graph.GraphExploreRequest
import org.elasticsearch.protocol.xpack.graph.GraphExploreResponse

suspend inline fun GraphClient.exploreAsync(options: RequestOptions = RequestOptions.DEFAULT, block: GraphExploreRequest.() -> Unit = {}): GraphExploreResponse =
        awaitAction { exploreAsync(options, it, block) }
