package com.suitebde.usecases.clubs

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.clubs.RoleInClub
import com.suitebde.models.clubs.UserInClub
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID
import dev.kaccelero.repositories.Pagination
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class ListUserInClubUseCaseTest {

    @Test
    fun testInvokeNoAssociationId() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val listUsersInClubUseCase = ListUsersInClubUseCase(client, getAssociationIdUseCase)
        coEvery { getAssociationIdUseCase() } returns null
        assertEquals(emptyList(), listUsersInClubUseCase(Pagination(0, 5), false, UUID()))
    }

    @Test
    fun testInvokeFromClient() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val listUsersInClubUseCase = ListUsersInClubUseCase(client, getAssociationIdUseCase)
        val associationId = UUID()
        val userInClub = UserInClub(
            userId = UUID(),
            clubId = UUID(),
            roleId = UUID(),
            user = null,
            club = null,
            role = RoleInClub(
                id = UUID(),
                clubId = UUID(),
                name = "name",
                admin = false,
                default = false
            )
        )
        coEvery { getAssociationIdUseCase() } returns associationId
        coEvery { client.usersInClubs.list(Pagination(0, 5), userInClub.clubId, associationId) } returns listOf(
            userInClub
        )
        assertEquals(listOf(userInClub), listUsersInClubUseCase(Pagination(0, 5), false, userInClub.clubId))
    }
}
