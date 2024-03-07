package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.suitebde.usecases.auth.IGetUserIdUseCase

class UpdateUserInClubUseCase(
    private val client: ISuiteBDEClient,
    private val getUserIdUseCase: IGetUserIdUseCase,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IUpdateUserInClubUseCase {
    override suspend fun invoke(input: Club): Club? {
        val userId = getUserIdUseCase() ?: return null
        val associationId = getAssociationIdUseCase() ?: return null

        if (input.isMember == true) client.usersInClubs.delete(userId, input.id, associationId)
        else client.usersInClubs.create(CreateUserInClubPayload(userId), input.id, associationId)
        return input.copy(
            usersCount = input.usersCount + if (input.isMember == true) -1 else 1,
            isMember = input.isMember != true
        )
    }
}