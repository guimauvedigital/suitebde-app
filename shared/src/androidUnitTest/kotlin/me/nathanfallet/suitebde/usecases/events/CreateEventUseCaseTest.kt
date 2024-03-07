package me.nathanfallet.suitebde.usecases.events

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.events.CreateEventPayload
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateEventUseCaseTest {

    private val event = Event(
        "id", "associationId", "name", "description",
        null, Instant.DISTANT_PAST, Instant.DISTANT_FUTURE, true
    )

    @Test
    fun testInvoke() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = CreateEventUseCase(client, getAssociationIdUseCase)
        val payload = CreateEventPayload("name", "description", null, Instant.DISTANT_PAST, Instant.DISTANT_FUTURE)
        every { getAssociationIdUseCase() } returns "associationId"
        coEvery { client.events.create(payload, "associationId") } returns event
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
