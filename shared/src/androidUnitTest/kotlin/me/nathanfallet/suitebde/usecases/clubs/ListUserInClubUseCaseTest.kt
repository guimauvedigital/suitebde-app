package me.nathanfallet.suitebde.usecases.clubs

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.clubs.RoleInClub
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.usecases.pagination.Pagination
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
