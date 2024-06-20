package com.suitebde.usecases.events

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.events.Event
import com.suitebde.repositories.events.IEventsRepository
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.repositories.Pagination
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class FetchEventsUseCaseTest {

    private val event = Event(
        "id", "associationId", "name", "description",
        null, Instant.DISTANT_PAST, Instant.DISTANT_FUTURE, true
    )

    @Test
    fun testInvokeNoAssociationSelected() = runBlocking {
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventsUseCase(mockk(), mockk(), getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns null
        assertEquals(emptyList(), useCase(Pagination(10, 5), false))
    }

    @Test
    fun testInvokeFromClient() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val eventsRepository = mockk<IEventsRepository>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventsUseCase(client, eventsRepository, getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns "associationId"
        every { eventsRepository.deleteExpired() } returns Unit
        every { eventsRepository.list(Pagination(10, 5)) } returns emptyList()
        coEvery { client.events.list(Pagination(10, 5), "associationId") } returns listOf(event)
        every { eventsRepository.save(event, any()) } returns Unit
        assertEquals(listOf(event), useCase(Pagination(10, 5), false))
        verify { eventsRepository.deleteExpired() }
        verify { eventsRepository.save(event, any()) }
    }

    @Test
    fun testInvokeFromCache() = runBlocking {
        val eventsRepository = mockk<IEventsRepository>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventsUseCase(mockk(), eventsRepository, getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns "associationId"
        every { eventsRepository.deleteExpired() } returns Unit
        every { eventsRepository.list(Pagination(10, 5)) } returns listOf(event)
        assertEquals(listOf(event), useCase(Pagination(10, 5), false))
        verify { eventsRepository.deleteExpired() }
    }

    @Test
    fun testInvokeFromClientForced() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val eventsRepository = mockk<IEventsRepository>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventsUseCase(client, eventsRepository, getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns "associationId"
        every { eventsRepository.deleteAll() } returns Unit
        every { eventsRepository.list(Pagination(10, 5)) } returns emptyList()
        coEvery { client.events.list(Pagination(10, 5), "associationId") } returns listOf(event)
        every { eventsRepository.save(event, any()) } returns Unit
        assertEquals(listOf(event), useCase(Pagination(10, 5), true))
        verify { eventsRepository.deleteAll() }
        verify { eventsRepository.save(event, any()) }
    }

}
