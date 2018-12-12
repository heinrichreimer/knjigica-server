@file:Suppress("UNUSED")

package de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.rest

import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.coroutines.awaitAction
import de.unihalle.informatik.bigdata.knjigica.indexer.util.elasticsearch.rest.*
import org.elasticsearch.action.support.master.AcknowledgedResponse
import org.elasticsearch.client.LicenseClient
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.license.*

suspend inline fun LicenseClient.putLicenseAsync(options: RequestOptions = RequestOptions.DEFAULT, block: PutLicenseRequest.() -> Unit = {}): PutLicenseResponse =
        awaitAction { putLicenseAsync(options, it, block) }

suspend inline fun LicenseClient.getLicenseAsync(options: RequestOptions = RequestOptions.DEFAULT, block: GetLicenseRequest.() -> Unit = {}): GetLicenseResponse =
        awaitAction { getLicenseAsync(options, it, block) }

suspend inline fun LicenseClient.deleteLicenseAsync(options: RequestOptions = RequestOptions.DEFAULT, block: DeleteLicenseRequest.() -> Unit = {}): AcknowledgedResponse =
        awaitAction { deleteLicenseAsync(options, it, block) }

suspend inline fun LicenseClient.startTrialAsync(options: RequestOptions = RequestOptions.DEFAULT, block: StartTrialRequest.() -> Unit = {}): StartTrialResponse =
        awaitAction { startTrialAsync(options, it, block) }

suspend inline fun LicenseClient.startBasicAsync(options: RequestOptions = RequestOptions.DEFAULT, block: StartBasicRequest.() -> Unit = {}): StartBasicResponse =
        awaitAction { startBasicAsync(options, it, block) }
