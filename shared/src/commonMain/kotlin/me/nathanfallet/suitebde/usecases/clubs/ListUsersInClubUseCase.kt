package me.nathanfallet.suitebde.usecases.clubs

import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.usecases.pagination.Pagination

class ListUsersInClubUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IListUsersInClubUseCase {

    override suspend fun invoke(input1: Pagination, input2: Boolean, input3: String): List<UserInClub> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.usersInClubs.list(input1, input3, associationId)
    }

}
