package com.suitebde.usecases.clubs

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.CreateUserInClubPayload
import com.suitebde.models.clubs.UserInClub
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import com.suitebde.usecases.auth.IGetUserIdUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateUserInClubUseCaseTest {
    @Test
    fun testInvokeNoAssociationId() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val getUserIdUseCase = mockk<IGetUserIdUseCase>()
        val createUserInClubUseCase = UpdateUserInClubUseCase(client, getUserIdUseCase, getAssociationIdUseCase)
        val club = Club(
            id = UUID(),
            associationId = UUID(),
            name = "name",
            description = "description",
            logo = null,
            createdAt = Clock.System.now(),
            validated = true,
            usersCount = 0,
            isMember = false,
        )
        coEvery { getAssociationIdUseCase() } returns null
        coEvery { getUserIdUseCase() } returns UUID()
        assertEquals(null, createUserInClubUseCase(club))
    }

    @Test
    fun testInvokeFromClientJoin() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val getUserIdUseCase = mockk<IGetUserIdUseCase>()
        val createUserInClubUseCase = UpdateUserInClubUseCase(client, getUserIdUseCase, getAssociationIdUseCase)
        val userPayload = CreateUserInClubPayload(UUID())
        val club = Club(
            id = UUID(),
            associationId = UUID(),
            name = "name",
            description = "description",
            logo = null,
            createdAt = Clock.System.now(),
            validated = true,
            usersCount = 0,
            isMember = false,
        )
        val userInClub = mockk<UserInClub>()
        coEvery { getAssociationIdUseCase() } returns club.associationId
        coEvery { getUserIdUseCase() } returns userPayload.userId
        coEvery { client.usersInClubs.create(userPayload, club.id, club.associationId) } returns userInClub
        assertEquals(Pair(club.copy(usersCount = 1, isMember = true), userInClub), createUserInClubUseCase(club))
    }

    @Test
    fun testInvokeFromClientLeave() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val getUserIdUseCase = mockk<IGetUserIdUseCase>()
        val createUserInClubUseCase = UpdateUserInClubUseCase(client, getUserIdUseCase, getAssociationIdUseCase)
        val userId = UUID()
        val club = Club(
            id = UUID(),
            associationId = UUID(),
            name = "name",
            description = "description",
            logo = null,
            createdAt = Clock.System.now(),
            validated = true,
            usersCount = 1,
            isMember = true,
        )
        coEvery { getAssociationIdUseCase() } returns club.associationId
        coEvery { getUserIdUseCase() } returns userId
        coEvery { client.usersInClubs.delete(userId, club.id, club.associationId) } returns true
        assertEquals(Pair(club.copy(usersCount = 0, isMember = false), null), createUserInClubUseCase(club))
    }
}
