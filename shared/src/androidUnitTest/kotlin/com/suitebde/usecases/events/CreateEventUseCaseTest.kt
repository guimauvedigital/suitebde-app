package com.suitebde.usecases.events

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.events.CreateEventPayload
import com.suitebde.models.events.Event
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateEventUseCaseTest {

    private val event = Event(
        UUID(), UUID(), "name", "description",
        null, Instant.DISTANT_PAST, Instant.DISTANT_FUTURE, true
    )

    @Test
    fun testInvoke() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = CreateEventUseCase(client, getAssociationIdUseCase)
        val payload = CreateEventPayload("name", "description", null, Instant.DISTANT_PAST, Instant.DISTANT_FUTURE)
        every { getAssociationIdUseCase() } returns event.associationId
        coEvery { client.events.create(payload, event.associationId) } returns event
        assertEquals(event, useCase(payload))
    }

    @Test
    fun testInvokeNoAssociationSelected() = runBlocking {
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = CreateEventUseCase(mockk(), getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns null
        assertEquals(null, useCase(mockk()))
    }

}
