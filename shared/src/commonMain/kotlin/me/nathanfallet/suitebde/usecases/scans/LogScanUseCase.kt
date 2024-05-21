package me.nathanfallet.suitebde.usecases.scans

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.scans.CreateScanPayload
import me.nathanfallet.suitebde.models.scans.Scan
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class LogScanUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : ILogScanUseCase {

    override suspend fun invoke(input: CreateScanPayload): Scan? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.scans.create(input, associationId)
    }

}
