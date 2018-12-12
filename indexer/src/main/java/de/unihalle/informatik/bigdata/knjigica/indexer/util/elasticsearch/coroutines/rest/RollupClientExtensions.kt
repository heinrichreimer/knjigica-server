@file:Suppress("UNUSED")

package de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.rest

import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.awaitAction
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.getRollupCapabilitiesAsync
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.getRollupJobAsync
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.putRollupJobAsync
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RollupClient
import org.elasticsearch.client.rollup.*
import org.elasticsearch.client.rollup.job.config.RollupJobConfig

suspend inline fun RollupClient.putRollupJobAsync(config: RollupJobConfig, options: RequestOptions = RequestOptions.DEFAULT, block: PutRollupJobRequest.() -> Unit = {}): PutRollupJobResponse =
        awaitAction { putRollupJobAsync(config, options, it, block) }

suspend inline fun RollupClient.getRollupJobAsync(options: RequestOptions = RequestOptions.DEFAULT, block: GetRollupJobRequest.() -> Unit = {}): GetRollupJobResponse =
        awaitAction { getRollupJobAsync(options, it, block) }

suspend inline fun RollupClient.getRollupCapabilitiesAsync(indexPattern: String, options: RequestOptions = RequestOptions.DEFAULT, block: GetRollupCapsRequest.() -> Unit = {}): GetRollupCapsResponse =
        awaitAction { getRollupCapabilitiesAsync(indexPattern, options, it, block) }
