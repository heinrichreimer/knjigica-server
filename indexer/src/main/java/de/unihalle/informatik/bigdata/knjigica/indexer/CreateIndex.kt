package de.unihalle.informatik.bigdata.knjigica.indexer

import de.unihalle.informatik.bigdata.knjigica.indexer.util.rest.indices
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.client.RequestOptions

object CreateIndex {

    @JvmStatic
    fun main(args: Array<String>) {
        Configuration.CLIENT.use {
            with(it) {
                indices {
                    create(CreateIndexRequest(Configuration.LIBRETTO_INDEX), RequestOptions.DEFAULT)
                }
            }
        }
    }
}