package com.suitebde.usecases.notifications

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.notifications.NotificationTopics
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class ListNotificationTopicsUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = ListNotificationTopicsUseCase(client, getAssociationIdUseCase)
        val topics = mockk<NotificationTopics>()
        val associationId = UUID()
        every { getAssociationIdUseCase() } returns associationId
        coEvery { client.notifications.topics(associationId) } returns topics
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
