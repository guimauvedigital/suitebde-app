package me.nathanfallet.suitebde.usecases.clubs

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteUserInClubUseCaseTest {

    @Test
    fun testInvokeNoAssociationId() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val deleteUserInClubUseCase = DeleteUserInClubUseCase(client, getAssociationIdUseCase)

        coEvery {
            getAssociationIdUseCase()
        } returns null

        assertEquals(
            false,
            deleteUserInClubUseCase("UserId", "ClubId")
        )
    }

    @Test
    fun testInvokeFromClient() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val deleteUserInClubUseCase = DeleteUserInClubUseCase(client, getAssociationIdUseCase)

        coEvery {
            getAssociationIdUseCase()
        } returns "AssociationId"

        coEvery {
            client.usersInClubs.delete("UserId", "ClubId", "AssociationId")
        } returns true

        assertEquals(
            true,
            deleteUserInClubUseCase("UserId", "ClubId")
        )
    }

    @Test
    fun testInvokeFromClientNonExistent() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val deleteUserInClubUseCase = DeleteUserInClubUseCase(client, getAssociationIdUseCase)

        coEvery {
            getAssociationIdUseCase()
        } returns "AssociationId"

        coEvery {
            client.usersInClubs.delete("UserId", "ClubId", "AssociationId")
        } returns false

        assertEquals(
            false,
            deleteUserInClubUseCase("UserId", "ClubId")
        )
    }
}