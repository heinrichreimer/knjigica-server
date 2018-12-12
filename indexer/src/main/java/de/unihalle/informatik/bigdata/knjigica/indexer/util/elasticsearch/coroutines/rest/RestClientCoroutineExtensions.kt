@file:Suppress("UNUSED")

package de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.rest

import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.awaitResponse
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.performRequestAsync
import org.elasticsearch.client.Request
import org.elasticsearch.client.Response
import org.elasticsearch.client.RestClient

suspend inline fun RestClient.performRequestAsync(method: String, endpoint: String, block: Request.() -> Unit = {}): Response =
        awaitResponse { performRequestAsync(method, endpoint, it, block) }
