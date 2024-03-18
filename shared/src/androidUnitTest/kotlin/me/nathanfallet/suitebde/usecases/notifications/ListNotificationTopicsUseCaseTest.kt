package me.nathanfallet.suitebde.usecases.notifications

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import me.nathanfallet.suitebde.client.ISuiteBDEClient
import me.nathanfallet.suitebde.models.notifications.NotificationTopics
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class ListNotificationTopicsUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = ListNotificationTopicsUseCase(client, getAssociationIdUseCase)
        val topics = mockk<NotificationTopics>()
        every { getAssociationIdUseCase() } returns "associationId"
        coEvery { client.notifications.topics("associationId") } returns topics
        assertEquals(topics, useCase())
    }

    @Test
    fun invokeNoAssociation() = runBlocking {
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = ListNotificationTopicsUseCase(mockk(), getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns null
        assertEquals(null, useCase())
    }

}
