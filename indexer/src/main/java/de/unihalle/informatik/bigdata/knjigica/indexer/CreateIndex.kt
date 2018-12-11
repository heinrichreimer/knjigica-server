package de.unihalle.informatik.bigdata.knjigica.indexer

import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.create
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.indices

object CreateIndex {

    @JvmStatic
    fun main(args: Array<String>) {
        Configuration.CLIENT.use {
            with(it) {
                indices {
                    create {
                        index(Configuration.LIBRETTO_INDEX)
                    }
                }
            }
        }
    }
}