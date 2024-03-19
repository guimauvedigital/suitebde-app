package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class FetchUserUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchUserUseCase {

    override suspend fun invoke(input1: String, input2: String?): User? {
        val associationId = input2 ?: getAssociationIdUseCase() ?: return null
        return client.users.get(input1, associationId)
    }

}
