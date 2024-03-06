package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.suitebde.usecases.auth.IGetUserIdUseCase

class DeleteUserInClubUseCase(
    private val client: ISuiteBDEClient,
    private val getUserIdUseCase: IGetUserIdUseCase,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IDeleteUserInClubUseCase {
    override suspend fun invoke(input: String): Boolean {
        val associationId = getAssociationIdUseCase() ?: return false
        val userId = getUserIdUseCase() ?: return false
        return client.usersInClubs.delete(userId, input, associationId)
    }
}