package de.unihalle.informatik.bigdata.knjigica.parser.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll

suspend fun <A, B> Iterable<A>.parallelMap(transform: suspend (A) -> B): List<B> {
    return map {
        GlobalScope.async {
            transform(it)
        }
    }.awaitAll()
}

suspend fun <A> Iterable<A>.parallelForEach(transform: suspend (A) -> Unit) {
    return map {
        GlobalScope.async {
            transform(it)
        }
    }.joinAll()
}