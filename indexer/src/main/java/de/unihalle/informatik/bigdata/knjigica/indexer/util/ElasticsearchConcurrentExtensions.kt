package de.unihalle.informatik.bigdata.knjigica.indexer.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.elasticsearch.action.ActionFuture
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        future: ActionFuture<*>
) = launch(context, start) { future.actionGet() }

fun ActionFuture<*>.launch(
        coroutineScope: CoroutineScope,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT
) = coroutineScope.launch(context, start) { actionGet() }

fun <T> CoroutineScope.async(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        future: ActionFuture<T>
) = async(context, start) { future.actionGet() }

fun <T> ActionFuture<T>.async(
        coroutineScope: CoroutineScope,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT
) = coroutineScope.async(context, start) { actionGet() }
