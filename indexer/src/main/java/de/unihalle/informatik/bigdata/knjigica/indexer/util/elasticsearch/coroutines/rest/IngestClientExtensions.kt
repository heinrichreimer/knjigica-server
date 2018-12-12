@file:Suppress("UNUSED")

package de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.rest

import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.awaitAction
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.deletePipelineAsync
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.getPipelineAsync
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.putPipelineAsync
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.simulateAsync
import org.elasticsearch.action.ingest.*
import org.elasticsearch.action.support.master.AcknowledgedResponse
import org.elasticsearch.client.IngestClient
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.common.bytes.BytesReference
import org.elasticsearch.common.xcontent.XContentType

suspend inline fun IngestClient.putPipelineAsync(id: String, source: BytesReference, xContentType: XContentType, options: RequestOptions = RequestOptions.DEFAULT, block: PutPipelineRequest.() -> Unit = {}): AcknowledgedResponse =
        awaitAction { putPipelineAsync(id, source, xContentType, options, it, block) }

suspend inline fun IngestClient.getPipelineAsync(options: RequestOptions = RequestOptions.DEFAULT, block: GetPipelineRequest.() -> Unit = {}): GetPipelineResponse =
        awaitAction { getPipelineAsync(options, it, block) }

suspend inline fun IngestClient.deletePipelineAsync(id: String, options: RequestOptions = RequestOptions.DEFAULT, block: DeletePipelineRequest.() -> Unit = {}): AcknowledgedResponse =
        awaitAction { deletePipelineAsync(id, options, it, block) }

suspend inline fun IngestClient.simulateAsync(source: BytesReference, xContentType: XContentType, options: RequestOptions = RequestOptions.DEFAULT, block: SimulatePipelineRequest.() -> Unit = {}): SimulatePipelineResponse =
        awaitAction { simulateAsync(source, xContentType, options, it, block) }
