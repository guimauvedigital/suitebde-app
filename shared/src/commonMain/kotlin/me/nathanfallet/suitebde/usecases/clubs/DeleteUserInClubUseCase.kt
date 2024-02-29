package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class DeleteUserInClubUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IDeleteUserInClubUseCase {
    override suspend fun invoke(input1: String, input2: String): Boolean {
        val associationId = getAssociationIdUseCase() ?: return false
        return client.usersInClubs.delete(input1, input2, associationId)
    }
}