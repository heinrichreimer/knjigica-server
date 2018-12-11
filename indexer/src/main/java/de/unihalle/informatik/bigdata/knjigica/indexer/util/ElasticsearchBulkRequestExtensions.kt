@file:Suppress("UNUSED")

package de.unihalle.informatik.bigdata.knjigica.indexer.util

import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.MultiSearchRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.update.UpdateRequest

fun MultiSearchRequest.search(index: String, type: String, block: SearchRequest.() -> Unit): MultiSearchRequest =
        add(SearchRequest().apply(block))

fun BulkRequest.index(index: String, type: String, block: IndexRequest.() -> Unit): BulkRequest =
        add(IndexRequest(index, type).apply(block))

fun BulkRequest.update(index: String, type: String, id: String, block: UpdateRequest.() -> Unit): BulkRequest =
        add(UpdateRequest(index, type, id).apply(block))

fun BulkRequest.delete(index: String, type: String, id: String, block: DeleteRequest.() -> Unit): BulkRequest =
        add(DeleteRequest(index, type, id).apply(block))
