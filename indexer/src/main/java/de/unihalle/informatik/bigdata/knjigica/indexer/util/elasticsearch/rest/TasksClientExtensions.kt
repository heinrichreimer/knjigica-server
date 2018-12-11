@file:Suppress("UNUSED")

package de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest

import org.elasticsearch.action.ActionListener
import org.elasticsearch.action.admin.cluster.node.tasks.cancel.CancelTasksRequest
import org.elasticsearch.action.admin.cluster.node.tasks.cancel.CancelTasksResponse
import org.elasticsearch.action.admin.cluster.node.tasks.list.ListTasksRequest
import org.elasticsearch.action.admin.cluster.node.tasks.list.ListTasksResponse
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.TasksClient
import java.io.IOException

@Throws(IOException::class)
inline fun TasksClient.list(options: RequestOptions = RequestOptions.DEFAULT, block: ListTasksRequest.() -> Unit): ListTasksResponse =
        list(ListTasksRequest().apply(block), options)

inline fun TasksClient.listAsync(options: RequestOptions = RequestOptions.DEFAULT, listener: ActionListener<ListTasksResponse>, block: ListTasksRequest.() -> Unit) =
        listAsync(ListTasksRequest().apply(block), options, listener)

@Throws(IOException::class)
inline fun TasksClient.cancel(options: RequestOptions = RequestOptions.DEFAULT, block: CancelTasksRequest.() -> Unit): CancelTasksResponse =
        cancel(CancelTasksRequest().apply(block), options)

inline fun TasksClient.cancelAsync(options: RequestOptions = RequestOptions.DEFAULT, listener: ActionListener<CancelTasksResponse>, block: CancelTasksRequest.() -> Unit) =
        cancelAsync(CancelTasksRequest().apply(block), options, listener)
