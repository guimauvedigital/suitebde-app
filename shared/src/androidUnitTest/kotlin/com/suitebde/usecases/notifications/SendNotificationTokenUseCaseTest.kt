package com.suitebde.usecases.notifications

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.notifications.CreateNotificationTokenPayload
import com.suitebde.models.notifications.NotificationToken
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import com.suitebde.usecases.auth.IGetUserIdUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class SendNotificationTokenUseCaseTest {

    @Test
    fun testInvoke() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getUserIdUseCase = mockk<IGetUserIdUseCase>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = SendNotificationTokenUseCase(client, getUserIdUseCase, getAssociationIdUseCase)
        val userId = UUID()
        val associationId = UUID()
        val token = NotificationToken("token", userId)
        every { getUserIdUseCase() } returns userId
        every { getAssociationIdUseCase() } returns associationId
        coEvery {
            client.notificationTokens.create(
                CreateNotificationTokenPayload("token"),
                userId,
                associationId
            )
        } returns token
        assertEquals(token, useCase("token"))
    }

    @Test
    fun invokeNoAssociation() = runBlocking {
        val getUserIdUseCase = mockk<IGetUserIdUseCase>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = SendNotificationTokenUseCase(mockk(), getUserIdUseCase, getAssociationIdUseCase)
        every { getUserIdUseCase() } returns UUID()
        every { getAssociationIdUseCase() } returns null
        assertEquals(null, useCase("token"))
    }

    @Test
    fun invokeNoUser() = runBlocking {
        val getUserIdUseCase = mockk<IGetUserIdUseCase>()
        val useCase = SendNotificationTokenUseCase(mockk(), getUserIdUseCase, mockk())
        every { getUserIdUseCase() } returns null
        assertEquals(null, useCase("token"))
    }

}
