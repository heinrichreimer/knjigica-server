@file:Suppress("UNUSED")

package de.unihalle.informatik.bigdata.knjigica.indexer.util.transport

import org.elasticsearch.action.ActionFuture
import org.elasticsearch.action.ActionListener
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.bulk.BulkResponse
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.delete.DeleteResponse
import org.elasticsearch.action.explain.ExplainRequest
import org.elasticsearch.action.explain.ExplainResponse
import org.elasticsearch.action.fieldcaps.FieldCapabilitiesRequest
import org.elasticsearch.action.fieldcaps.FieldCapabilitiesResponse
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.get.MultiGetRequest
import org.elasticsearch.action.get.MultiGetResponse
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.search.*
import org.elasticsearch.action.termvectors.MultiTermVectorsRequest
import org.elasticsearch.action.termvectors.MultiTermVectorsResponse
import org.elasticsearch.action.termvectors.TermVectorsRequest
import org.elasticsearch.action.termvectors.TermVectorsResponse
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.action.update.UpdateResponse
import org.elasticsearch.client.AdminClient
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings

inline val Client.settings: Settings
    get() = settings()

fun Client.filterWithHeader(vararg headers: Pair<String, String>): Client {
    return filterWithHeader(mapOf(*headers))
}

fun <R> Client.admin(block: AdminClient.() -> R) = admin().run(block)

inline fun Client.index(block: IndexRequest.() -> Unit): ActionFuture<IndexResponse> =
        index(IndexRequest().apply(block))

inline fun Client.index(listener: ActionListener<IndexResponse>, block: IndexRequest.() -> Unit) =
        index(IndexRequest().apply(block), listener)

inline fun Client.update(block: UpdateRequest.() -> Unit): ActionFuture<UpdateResponse> =
        update(UpdateRequest().apply(block))

inline fun Client.update(listener: ActionListener<UpdateResponse>, block: UpdateRequest.() -> Unit) =
        update(UpdateRequest().apply(block), listener)

inline fun Client.delete(block: DeleteRequest.() -> Unit): ActionFuture<DeleteResponse> =
        delete(DeleteRequest().apply(block))

inline fun Client.delete(listener: ActionListener<DeleteResponse>, block: DeleteRequest.() -> Unit) =
        delete(DeleteRequest().apply(block), listener)

inline fun Client.bulk(block: BulkRequest.() -> Unit): ActionFuture<BulkResponse> =
        bulk(BulkRequest().apply(block))

inline fun Client.bulk(listener: ActionListener<BulkResponse>, block: BulkRequest.() -> Unit) =
        bulk(BulkRequest().apply(block), listener)

inline fun Client.get(block: GetRequest.() -> Unit): ActionFuture<GetResponse> =
        get(GetRequest().apply(block))

inline fun Client.get(listener: ActionListener<GetResponse>, block: GetRequest.() -> Unit) =
        get(GetRequest().apply(block), listener)

inline fun Client.multiGet(block: MultiGetRequest.() -> Unit): ActionFuture<MultiGetResponse> =
        multiGet(MultiGetRequest().apply(block))

inline fun Client.multiGet(listener: ActionListener<MultiGetResponse>, block: MultiGetRequest.() -> Unit) =
        multiGet(MultiGetRequest().apply(block), listener)

inline fun Client.search(block: SearchRequest.() -> Unit): ActionFuture<SearchResponse> =
        search(SearchRequest().apply(block))

inline fun Client.search(listener: ActionListener<SearchResponse>, block: SearchRequest.() -> Unit) =
        search(SearchRequest().apply(block), listener)

inline fun Client.searchScroll(block: SearchScrollRequest.() -> Unit): ActionFuture<SearchResponse> =
        searchScroll(SearchScrollRequest().apply(block))

inline fun Client.searchScroll(listener: ActionListener<SearchResponse>, block: SearchScrollRequest.() -> Unit) =
        searchScroll(SearchScrollRequest().apply(block), listener)

inline fun Client.multiSearch(block: MultiSearchRequest.() -> Unit): ActionFuture<MultiSearchResponse> =
        multiSearch(MultiSearchRequest().apply(block))

inline fun Client.multiSearch(listener: ActionListener<MultiSearchResponse>, block: MultiSearchRequest.() -> Unit) =
        multiSearch(MultiSearchRequest().apply(block), listener)

inline fun Client.termVectors(block: TermVectorsRequest.() -> Unit): ActionFuture<TermVectorsResponse> =
        termVectors(TermVectorsRequest().apply(block))

inline fun Client.termVectors(listener: ActionListener<TermVectorsResponse>, block: TermVectorsRequest.() -> Unit) =
        termVectors(TermVectorsRequest().apply(block), listener)

inline fun Client.multiTermVectors(block: MultiTermVectorsRequest.() -> Unit): ActionFuture<MultiTermVectorsResponse> =
        multiTermVectors(MultiTermVectorsRequest().apply(block))

inline fun Client.multiTermVectors(listener: ActionListener<MultiTermVectorsResponse>, block: MultiTermVectorsRequest.() -> Unit) =
        multiTermVectors(MultiTermVectorsRequest().apply(block), listener)

inline fun Client.explain(index: String, type: String, id: String, block: ExplainRequest.() -> Unit): ActionFuture<ExplainResponse> =
        explain(ExplainRequest().apply(block))

inline fun Client.explain(index: String, type: String, id: String, listener: ActionListener<ExplainResponse>, block: ExplainRequest.() -> Unit) =
        explain(ExplainRequest().apply(block), listener)

inline fun Client.clearScroll(block: ClearScrollRequest.() -> Unit): ActionFuture<ClearScrollResponse> =
        clearScroll(ClearScrollRequest().apply(block))

inline fun Client.clearScroll(listener: ActionListener<ClearScrollResponse>, block: ClearScrollRequest.() -> Unit) =
        clearScroll(ClearScrollRequest().apply(block), listener)

inline fun Client.fieldCaps(block: FieldCapabilitiesRequest.() -> Unit): ActionFuture<FieldCapabilitiesResponse> =
        fieldCaps(FieldCapabilitiesRequest().apply(block))

inline fun Client.fieldCaps(listener: ActionListener<FieldCapabilitiesResponse>, block: FieldCapabilitiesRequest.() -> Unit) =
        fieldCaps(FieldCapabilitiesRequest().apply(block), listener)
