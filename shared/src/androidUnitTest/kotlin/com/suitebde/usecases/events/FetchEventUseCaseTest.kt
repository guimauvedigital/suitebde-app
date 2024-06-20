package com.suitebde.usecases.events

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.events.Event
import com.suitebde.repositories.events.IEventsRepository
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class FetchEventUseCaseTest {

    private val event = Event(
        "id", "associationId", "name", "description",
        null, Instant.DISTANT_PAST, Instant.DISTANT_FUTURE, true
    )

    @Test
    fun testInvokeNoAssociationSelected() = runBlocking {
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventUseCase(mockk(), mockk(), getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns null
        assertEquals(null, useCase("id", false))
    }

    @Test
    fun testInvokeFromClient() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val eventsRepository = mockk<IEventsRepository>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventUseCase(client, eventsRepository, getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns "associationId"
        every { eventsRepository.get("id") } returns null
        coEvery { client.events.get("id", "associationId") } returns event
        every { eventsRepository.save(event, any()) } returns Unit
        assertEquals(event, useCase("id", false))
        verify { eventsRepository.save(event, any()) }
    }

    @Test
    fun testInvokeFromCache() = runBlocking {
        val eventsRepository = mockk<IEventsRepository>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventUseCase(mockk(), eventsRepository, getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns "associationId"
        every { eventsRepository.get("id") } returns event
        assertEquals(event, useCase("id", false))
    }

    @Test
    fun testInvokeFromClientForced() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val eventsRepository = mockk<IEventsRepository>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventUseCase(client, eventsRepository, getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns "associationId"
        every { eventsRepository.delete("id") } returns Unit
        every { eventsRepository.get("id") } returns null
        every { eventsRepository.save(event, any()) } returns Unit
        coEvery { client.events.get("id", "associationId") } returns event
        assertEquals(event, useCase("id", true))
        verify { eventsRepository.delete("id") }
        verify { eventsRepository.save(event, any()) }
    }

}
