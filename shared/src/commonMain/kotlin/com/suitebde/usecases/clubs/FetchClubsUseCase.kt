package com.suitebde.usecases.clubs

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.clubs.Club
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.repositories.Pagination

class FetchClubsUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchClubsUseCase {

    override suspend fun invoke(input1: Pagination, input2: Boolean): List<Club> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.clubs.list(input1, associationId)
    }

}
