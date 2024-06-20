package com.suitebde.usecases.users

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.users.User
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.repositories.Pagination

class FetchUsersUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IFetchUsersUseCase {

    override suspend fun invoke(input: Pagination): List<User> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.users.list(input, associationId)
    }

}
