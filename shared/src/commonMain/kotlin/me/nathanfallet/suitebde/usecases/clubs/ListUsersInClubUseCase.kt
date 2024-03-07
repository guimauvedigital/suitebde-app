package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class ListUsersInClubUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IListUsersInClubUseCase {
    override suspend fun invoke(input1: Long, input2: Long, input3: Boolean, input4: String): List<UserInClub> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.usersInClubs.list(input1, input2, input4, associationId)
    }
}