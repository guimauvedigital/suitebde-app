package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class FetchClubUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchClubUseCase {
    override suspend fun invoke(input: String): Club? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.clubs.get(input, associationId)
    }
}