package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.suitebde.usecases.auth.IGetUserIdUseCase

class UpdateUserInClubUseCase(
    private val client: ISuiteBDEClient,
    private val getUserIdUseCase: IGetUserIdUseCase,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IUpdateUserInClubUseCase {

    override suspend fun invoke(input: Club): Pair<Club, UserInClub?>? {
        val userId = getUserIdUseCase() ?: return null
        val associationId = getAssociationIdUseCase() ?: return null

        return if (input.isMember == true) Pair(
            input.copy(usersCount = input.usersCount - 1, isMember = false),
            client.usersInClubs.delete(userId, input.id, associationId).let { null }
        ) else Pair(
            input.copy(usersCount = input.usersCount + 1, isMember = true),
            client.usersInClubs.create(CreateUserInClubPayload(userId), input.id, associationId)
        )
    }

}
