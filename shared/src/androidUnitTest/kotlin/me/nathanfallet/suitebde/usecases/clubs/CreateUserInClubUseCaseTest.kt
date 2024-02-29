package me.nathanfallet.suitebde.usecases.clubs

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.clubs.CreateUserInClubPayload
import me.nathanfallet.suitebde.models.clubs.RoleInClub
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateUserInClubUseCaseTest {

    @Test
    fun testInvokeNoAssociationId() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val createUserInClubUseCase = CreateUserInClubUseCase(client, getAssociationIdUseCase)

        coEvery {
            getAssociationIdUseCase()
        } returns null

        assertEquals(
            null,
            createUserInClubUseCase(
                CreateUserInClubPayload(
                    "userId"
                ),
                "clubId"
            )
        )
    }

    @Test
    fun testInvokeFromClient() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val createUserInClubUseCase = CreateUserInClubUseCase(client, getAssociationIdUseCase)

        val userPayload = CreateUserInClubPayload(
            "userId"
        )

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

        coEvery {
            getAssociationIdUseCase()
        } returns "associationId"

        coEvery {
            client.usersInClubs.create(userPayload, "clubId", "associationId")
        } returns userInClub

        assertEquals(
            userInClub,
            createUserInClubUseCase(userPayload, "clubId")
        )
    }

}