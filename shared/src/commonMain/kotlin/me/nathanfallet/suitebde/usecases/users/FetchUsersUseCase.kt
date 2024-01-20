package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class FetchUsersUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchUsersUseCase {

    override suspend fun invoke(input1: Long, input2: Long): List<User> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.users.list(input1, input2, associationId)
    }

}
