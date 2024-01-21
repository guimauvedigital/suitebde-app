package me.nathanfallet.suitebde.usecases.events

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class FetchEventsUseCaseTest {

    private val event = Event(
        "id", "associationId", "name", "description",
        null, Instant.DISTANT_PAST, Instant.DISTANT_FUTURE, true
    )

    @Test
    fun testInvoke() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventsUseCase(client, getAssociationIdUseCase)
        every { getAssociationIdUseCase.invoke() } returns "associationId"
        coEvery { client.events.list(10, 5, "associationId") } returns listOf(event)
        assertEquals(listOf(event), useCase.invoke(10, 5))
    }

    @Test
    fun testInvokeNoAssociationSelected() = runBlocking {
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = FetchEventsUseCase(mockk(), getAssociationIdUseCase)
        every { getAssociationIdUseCase.invoke() } returns null
        assertEquals(emptyList(), useCase.invoke(10, 5))
    }

}
