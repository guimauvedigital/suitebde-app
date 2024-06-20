package com.suitebde.usecases.scans

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.scans.CreateScanPayload
import com.suitebde.models.scans.Scan
import com.suitebde.usecases.auth.IGetAssociationIdUseCase

class LogScanUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : ILogScanUseCase {

    override suspend fun invoke(input: CreateScanPayload): Scan? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.scans.create(input, associationId)
    }

}
