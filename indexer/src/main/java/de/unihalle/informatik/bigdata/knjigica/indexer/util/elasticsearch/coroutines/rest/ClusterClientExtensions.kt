@file:Suppress("UNUSED")

package de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.rest

import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.awaitAction
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.getSettingsAsync
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.healthAsync
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.putSettingsAsync
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsRequest
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsResponse
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsRequest
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsResponse
import org.elasticsearch.client.ClusterClient
import org.elasticsearch.client.RequestOptions

suspend inline fun ClusterClient.putSettingsAsync(options: RequestOptions = RequestOptions.DEFAULT, block: ClusterUpdateSettingsRequest.() -> Unit = {}): ClusterUpdateSettingsResponse =
        awaitAction { putSettingsAsync(options, it, block) }

suspend inline fun ClusterClient.getSettingsAsync(options: RequestOptions = RequestOptions.DEFAULT, block: ClusterGetSettingsRequest.() -> Unit = {}): ClusterGetSettingsResponse =
        awaitAction { getSettingsAsync(options, it, block) }

suspend inline fun ClusterClient.healthAsync(options: RequestOptions = RequestOptions.DEFAULT, block: ClusterHealthRequest.() -> Unit = {}): ClusterHealthResponse =
        awaitAction { healthAsync(options, it, block) }
