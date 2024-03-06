package me.nathanfallet.suitebde.usecases.clubs

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import me.nathanfallet.suitebde.usecases.auth.IGetUserIdUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteUserInClubUseCaseTest {

    @Test
    fun testInvokeNoAssociationId() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val getUserIdUseCase = mockk<IGetUserIdUseCase>()
        val deleteUserInClubUseCase = DeleteUserInClubUseCase(client, getUserIdUseCase, getAssociationIdUseCase)

        coEvery {
            getAssociationIdUseCase()
        } returns null

        assertEquals(
            false,
            deleteUserInClubUseCase("ClubId")
        )
    }

    @Test
    fun testInvokeFromClient() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val getUserIdUseCase = mockk<IGetUserIdUseCase>()
        val deleteUserInClubUseCase = DeleteUserInClubUseCase(client, getUserIdUseCase, getAssociationIdUseCase)

        coEvery {
            getAssociationIdUseCase()
        } returns "AssociationId"

        coEvery {
            getUserIdUseCase()
        } returns "UserId"

        coEvery {
            client.usersInClubs.delete("UserId", "ClubId", "AssociationId")
        } returns true

        assertEquals(
            true,
            deleteUserInClubUseCase("ClubId")
        )
    }

    @Test
    fun testInvokeFromClientNonExistent() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val getUserIdUseCase = mockk<IGetUserIdUseCase>()
        val deleteUserInClubUseCase = DeleteUserInClubUseCase(client, getUserIdUseCase, getAssociationIdUseCase)

        coEvery {
            getAssociationIdUseCase()
        } returns "AssociationId"

        coEvery {
            getUserIdUseCase()
        } returns "UserId"

        coEvery {
            client.usersInClubs.delete("UserId", "ClubId", "AssociationId")
        } returns false

        assertEquals(
            false,
            deleteUserInClubUseCase("ClubId")
        )
    }
}