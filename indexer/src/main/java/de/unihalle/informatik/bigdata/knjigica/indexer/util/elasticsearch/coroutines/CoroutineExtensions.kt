package de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines

import kotlinx.coroutines.suspendCancellableCoroutine
import org.elasticsearch.action.ActionFuture
import org.elasticsearch.action.ActionListener
import org.elasticsearch.client.Response
import org.elasticsearch.client.ResponseListener
import java.util.concurrent.ExecutionException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T> ActionFuture<T>.await(): T = suspendCoroutine { continuation ->
    try {
        continuation.resume(get())
    } catch (cause: ExecutionException) {
        continuation.resumeWithException(cause)
    } catch (cause: InterruptedException) {
        continuation.resumeWithException(cause)
    }
}

@JvmName("awaitResponseReceiver")
suspend inline fun ((ResponseListener) -> Unit).await(): Response = suspendCancellableCoroutine { continuation ->
    this(object : ResponseListener {
        override fun onSuccess(response: Response) = continuation.resume(response)
        override fun onFailure(exception: Exception) = continuation.resumeWithException(exception)
    })
}

suspend inline fun awaitResponse(block: (ResponseListener) -> Unit): Response = block.await()

@JvmName("awaitActionReceiver")
suspend inline fun <T> ((ActionListener<T>) -> Unit).await(): T = suspendCancellableCoroutine { continuation ->
    this(object : ActionListener<T> {
        override fun onResponse(response: T) = continuation.resume(response)
        override fun onFailure(exception: Exception) = continuation.resumeWithException(exception)
    })
}

suspend inline fun <T> awaitAction(block: (ActionListener<T>) -> Unit): T = block.await()