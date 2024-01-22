package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class FetchUserUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchUserUseCase {

    override suspend fun invoke(input: String): User? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.users.get(input, associationId)
    }

}
