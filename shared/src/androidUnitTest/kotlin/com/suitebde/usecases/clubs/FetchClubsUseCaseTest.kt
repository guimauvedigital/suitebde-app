package com.suitebde.usecases.clubs

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.clubs.Club
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.repositories.Pagination
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class FetchClubsUseCaseTest {
    @Test
    fun testInvokeNoAssociationId() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val fetchClubsUseCase = FetchClubsUseCase(client, getAssociationIdUseCase)

        coEvery { getAssociationIdUseCase() } returns null

        assertEquals(emptyList(), fetchClubsUseCase(Pagination(0, 5), false))
    }

    @Test
    fun testInvokeFromClient() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val fetchClubsUseCase = FetchClubsUseCase(client, getAssociationIdUseCase)

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

        coEvery { getAssociationIdUseCase() } returns "associationId"
        coEvery { client.clubs.list(Pagination(0, 5), "associationId") } returns listOf(club)

        assertEquals(listOf(club), fetchClubsUseCase(Pagination(0, 5), false))
    }
}
