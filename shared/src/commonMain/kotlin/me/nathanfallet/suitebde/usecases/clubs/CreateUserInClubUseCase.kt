package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class CreateUserInClubUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : ICreateUserInClubUseCase {
    override suspend fun invoke(input1: CreateUserInClubPayload, input2: String): UserInClub? {
        val associationId = getAssociationIdUseCase() ?: return null
        return client.usersInClubs.create(input1, input2, associationId)
    }
}