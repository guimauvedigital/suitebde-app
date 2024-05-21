package me.nathanfallet.suitebde.usecases.scans

import kotlinx.datetime.LocalDate
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.scans.ScansForDay
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class FetchScansUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchScansUseCase {

    override suspend fun invoke(input1: LocalDate, input2: LocalDate): List<ScansForDay> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.scans.list(input1, input2, associationId)
    }

}
