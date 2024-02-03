package me.nathanfallet.suitebde.usecases.events

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.repositories.events.IEventsRepository
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
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
        every { getAssociationIdUseCase.invoke() } returns null
        assertEquals(null, useCase.invoke("id", false))
    }

    @Test
    fun testInvokeFromClient() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val eventsRepository = mockk<IEventsRepository>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventUseCase(client, eventsRepository, getAssociationIdUseCase)
        every { getAssociationIdUseCase.invoke() } returns "associationId"
        every { eventsRepository.get("id") } returns null
        coEvery { client.events.get("id", "associationId") } returns event
        every { eventsRepository.save(event, any()) } returns Unit
        assertEquals(event, useCase.invoke("id", false))
        verify { eventsRepository.save(event, any()) }
    }

    @Test
    fun testInvokeFromCache() = runBlocking {
        val eventsRepository = mockk<IEventsRepository>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventUseCase(mockk(), eventsRepository, getAssociationIdUseCase)
        every { getAssociationIdUseCase.invoke() } returns "associationId"
        every { eventsRepository.get("id") } returns event
        assertEquals(event, useCase.invoke("id", false))
    }

    @Test
    fun testInvokeFromClientForced() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val eventsRepository = mockk<IEventsRepository>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventUseCase(client, eventsRepository, getAssociationIdUseCase)
        every { getAssociationIdUseCase.invoke() } returns "associationId"
        every { eventsRepository.delete("id") } returns Unit
        every { eventsRepository.get("id") } returns null
        every { eventsRepository.save(event, any()) } returns Unit
        coEvery { client.events.get("id", "associationId") } returns event
        assertEquals(event, useCase.invoke("id", true))
        verify { eventsRepository.delete("id") }
        verify { eventsRepository.save(event, any()) }
    }

}
