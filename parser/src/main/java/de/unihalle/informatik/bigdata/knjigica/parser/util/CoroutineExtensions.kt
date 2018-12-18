package de.unihalle.informatik.bigdata.knjigica.parser.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll
import kotlin.coroutines.coroutineContext

suspend fun <A, B> Iterable<A>.parallelMap(transform: suspend (A) -> B): List<B> {
    return map {
        GlobalScope.async(coroutineContext) {
            transform(it)
        }
    }.awaitAll()
}

suspend fun <A> Iterable<A>.parallelForEach(transform: suspend (A) -> Unit): Unit {
    return map {
        GlobalScope.async(coroutineContext) {
            transform(it)
        }
    }.joinAll()
}