package com.suitebde.usecases.events

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.events.Event
import com.suitebde.models.events.UpdateEventPayload
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateEventUseCaseTest {

    private val event = Event(
        UUID(), UUID(), "name", "description",
        null, Instant.DISTANT_PAST, Instant.DISTANT_FUTURE, true
    )

    @Test
    fun testInvoke() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = UpdateEventUseCase(client, getAssociationIdUseCase)
        val payload = UpdateEventPayload("name", "description", null, Instant.DISTANT_PAST, Instant.DISTANT_FUTURE)
        every { getAssociationIdUseCase() } returns event.associationId
        coEvery { client.events.update(event.id, payload, event.associationId) } returns event
        assertEquals(event, useCase(event.id, payload))
    }

    @Test
    fun testInvokeNoAssociationSelected() = runBlocking {
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = UpdateEventUseCase(mockk(), getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns null
        assertEquals(null, useCase(event.id, mockk()))
    }

}
