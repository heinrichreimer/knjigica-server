package de.unihalle.informatik.bigdata.knjigica.indexer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

object TestIndexer {

    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking(Dispatchers.IO) {
            Configuration.CLIENT.use { client ->
                OperaLibIndexer.index(client)
            }
        }
    }
}