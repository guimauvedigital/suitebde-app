package com.suitebde.usecases.notifications

import com.suitebde.client.ISuiteBDEClient
import com.suitebde.models.notifications.CreateNotificationPayload
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class SendNotificationUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val client = mockk<ISuiteBDEClient>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = SendNotificationUseCase(client, getAssociationIdUseCase)
        val payload = mockk<CreateNotificationPayload>()
        every { getAssociationIdUseCase() } returns "associationId"
        coEvery { client.notifications.send(payload, "associationId") } returns Unit
        useCase(payload)
        coVerify { client.notifications.send(payload, "associationId") }
    }

    @Test
    fun invokeNoAssociation() = runBlocking {
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val useCase = SendNotificationUseCase(mockk(), getAssociationIdUseCase)
        every { getAssociationIdUseCase() } returns null
        useCase(mockk()) // Checks that client is not called (as not mocked, it would fail if called)
    }

}
