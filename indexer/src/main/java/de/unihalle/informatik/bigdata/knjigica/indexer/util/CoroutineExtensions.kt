package de.unihalle.informatik.bigdata.knjigica.indexer.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlin.coroutines.coroutineContext

suspend fun <A, B> Iterable<A>.parallelMap(transform: suspend (A) -> B): List<B> {
    return map {
        GlobalScope.async(coroutineContext) {
            transform(it)
        }
    }.awaitAll()
}