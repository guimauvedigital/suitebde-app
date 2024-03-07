package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.usecases.pagination.Pagination

class FetchClubsUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchClubsUseCase {

    override suspend fun invoke(input1: Pagination, input2: Boolean): List<Club> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.clubs.list(input1, associationId)
    }

}
