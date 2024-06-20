package com.suitebde.usecases.clubs

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.clubs.Club
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID

class FetchClubUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchClubUseCase {

    override suspend fun invoke(input: UUID): Club? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.clubs.get(input, associationId)
    }

}
