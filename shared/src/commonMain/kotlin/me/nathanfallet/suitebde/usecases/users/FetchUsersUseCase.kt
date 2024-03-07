package me.nathanfallet.suitebde.usecases.users

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.usecases.pagination.Pagination

class FetchUsersUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchUsersUseCase {

    override suspend fun invoke(input: Pagination): List<User> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.users.list(input, associationId)
    }

}
