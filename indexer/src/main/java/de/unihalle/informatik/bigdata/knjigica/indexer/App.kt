package de.unihalle.informatik.bigdata.knjigica.indexer

import de.unihalle.informatik.bigdata.knjigica.indexer.util.index
import de.unihalle.informatik.bigdata.knjigica.indexer.util.rest.bulk
import de.unihalle.informatik.bigdata.knjigica.indexer.util.rest.index
import java.net.UnknownHostException

object App {
    @Throws(UnknownHostException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        Configuration.CLIENT.use {
            with(it) {
                println(lowLevelClient.nodes)
                bulk {
                    for (i in 0..100_000) {
                        index(Configuration.LIBRETTO_INDEX, Configuration.LIBRETTO_TYPE) {
                            source("ddssdg", "sdssdv")
                        }
                    }
                }
                index(Configuration.LIBRETTO_INDEX, Configuration.LIBRETTO_TYPE) {
                    source("ddssdg", "sdssdv")
                }
            }
        }


    }
}