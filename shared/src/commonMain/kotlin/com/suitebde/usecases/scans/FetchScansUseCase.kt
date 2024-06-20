package com.suitebde.usecases.scans

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.scans.ScansForDay
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import kotlinx.datetime.LocalDate

class FetchScansUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchScansUseCase {

    override suspend fun invoke(input1: LocalDate, input2: LocalDate): List<ScansForDay> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.scans.list(input1, input2, associationId)
    }

}
