package de.unihalle.informatik.bigdata.knjigica.indexer.util

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend inline fun <A, B> Iterable<A>.parallelMap(crossinline transform: suspend (A) -> B): List<B> {
    return coroutineScope {
        map {
            async(coroutineContext) {
                transform(it)
            }
        }
    }.awaitAll()
}

suspend inline fun <A, B> Sequence<A>.parallelMap(crossinline transform: suspend (A) -> B): List<B> {
    return coroutineScope {
        map {
            async(coroutineContext) {
                transform(it)
            }
        }
    }.toList().awaitAll()
}