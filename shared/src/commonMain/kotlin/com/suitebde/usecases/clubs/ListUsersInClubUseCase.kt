package com.suitebde.usecases.clubs

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.clubs.UserInClub
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination

class ListUsersInClubUseCase(
    private val client: ISuiteBDEClient,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
) : IListUsersInClubUseCase {

    override suspend fun invoke(input1: Pagination, input2: Boolean, input3: UUID): List<UserInClub> {
        val associationId = getAssociationIdUseCase() ?: return emptyList()
        return client.usersInClubs.list(input1, input3, associationId)
    }

}
