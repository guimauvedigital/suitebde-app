package com.suitebde.usecases.clubs

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.clubs.RoleInClub
import com.suitebde.models.clubs.UserInClub
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
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

        assertEquals(emptyList(), listUsersInClubUseCase(Pagination(0, 5), false, "clubId"))
    }

    @Test
    fun testInvokeFromClient() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val listUsersInClubUseCase = ListUsersInClubUseCase(client, getAssociationIdUseCase)

        val userInClub = UserInClub(
            userId = "userId",
            clubId = "clubId",
            roleId = "roleId",
            user = null,
            club = null,
            role = RoleInClub(
                id = "roleId",
                clubId = "clubId",
                name = "name",
                admin = false,
                default = false
            )
        )

        coEvery { getAssociationIdUseCase() } returns "associationId"
        coEvery { client.usersInClubs.list(Pagination(0, 5), "clubId", "associationId") } returns listOf(userInClub)

        assertEquals(listOf(userInClub), listUsersInClubUseCase(Pagination(0, 5), false, "clubId"))
    }
}
