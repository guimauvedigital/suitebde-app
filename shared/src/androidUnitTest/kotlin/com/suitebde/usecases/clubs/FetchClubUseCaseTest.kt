package com.suitebde.usecases.clubs

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.clubs.Club
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test

class FetchClubUseCaseTest {

    @Test
    fun testInvokeNoAssociationId() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val fetchClubUseCase = FetchClubUseCase(client, getAssociationIdUseCase)

        coEvery {
            getAssociationIdUseCase()
        } returns null

        assertEquals(
            null,
            fetchClubUseCase("id1")
        )
    }

    @Test
    fun testInvokeFromClient() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val fetchClubUseCase = FetchClubUseCase(client, getAssociationIdUseCase)

        val club = Club(
            id = "id1",
            associationId = "associationId",
            name = "name1",
            description = "description1",
            logo = null,
            createdAt = Clock.System.now(),
            validated = true,
            usersCount = 0,
            isMember = null
        )

        coEvery {
            getAssociationIdUseCase()
        } returns "associationId"

        coEvery {
            client.clubs.get("id1", "associationId")
        } returns club

        assertEquals(
            club,
            fetchClubUseCase("id1")
        )
    }
}
