@file:Suppress("UNUSED")

package de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest

import org.elasticsearch.client.MigrationClient
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.protocol.xpack.migration.IndexUpgradeInfoRequest
import org.elasticsearch.protocol.xpack.migration.IndexUpgradeInfoResponse
import java.io.IOException

@Throws(IOException::class)
inline fun MigrationClient.getAssistance(options: RequestOptions = RequestOptions.DEFAULT, block: IndexUpgradeInfoRequest.() -> Unit = {}): IndexUpgradeInfoResponse =
        getAssistance(IndexUpgradeInfoRequest().apply(block), options)
