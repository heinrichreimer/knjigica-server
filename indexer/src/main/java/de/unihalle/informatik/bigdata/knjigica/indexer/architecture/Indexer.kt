package de.unihalle.informatik.bigdata.knjigica.indexer.architecture

import org.elasticsearch.client.RestHighLevelClient

interface Indexer<T> {
    suspend fun index(client: RestHighLevelClient, items: Iterable<T>)
}