package com.suitebde.usecases.notifications

import com.suitebde.models.notifications.SubscribeToNotificationTopicType
import com.suitebde.usecases.auth.IGetAssociationIdUseCase
import dev.kaccelero.models.UUID
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class SetupNotificationsUseCaseTest {

    @Test
    fun invoke() = runBlocking {
        val getFcmTokenUseCase = mockk<IGetFcmTokenUseCase>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val subscribeToNotificationTopicUseCase = mockk<ISubscribeToNotificationTopicUseCase>()
        val sendNotificationTokenUseCase = mockk<ISendNotificationTokenUseCase>()
        val useCase = SetupNotificationsUseCase(
            getFcmTokenUseCase,
            getAssociationIdUseCase,
            subscribeToNotificationTopicUseCase,
            sendNotificationTokenUseCase
        )
        val associationId = UUID()
        every { getFcmTokenUseCase() } returns "fcmToken"
        coEvery { sendNotificationTokenUseCase("fcmToken") } returns mockk()
        every { getAssociationIdUseCase() } returns associationId
        every {
            subscribeToNotificationTopicUseCase(
                "associations_$associationId",
                SubscribeToNotificationTopicType.SUBSCRIBE
            )
        } returns Unit
        every {
            subscribeToNotificationTopicUseCase(
                "associations_${associationId}_events",
                SubscribeToNotificationTopicType.AS_SAVED
            )
        } returns Unit
        useCase()
        coVerify { sendNotificationTokenUseCase("fcmToken") }
        coVerify {
            subscribeToNotificationTopicUseCase(
                "associations_$associationId",
                SubscribeToNotificationTopicType.SUBSCRIBE
            )
        }
        coVerify {
            subscribeToNotificationTopicUseCase(
                "associations_${associationId}_events",
                SubscribeToNotificationTopicType.AS_SAVED
            )
        }
    }

    @Test
    fun invokeNoAssociationId() = runBlocking {
        val getFcmTokenUseCase = mockk<IGetFcmTokenUseCase>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val sendNotificationTokenUseCase = mockk<ISendNotificationTokenUseCase>()
        val useCase = SetupNotificationsUseCase(
            getFcmTokenUseCase, getAssociationIdUseCase, mockk(), sendNotificationTokenUseCase
        )
        every { getFcmTokenUseCase() } returns "fcmToken"
        coEvery { sendNotificationTokenUseCase("fcmToken") } returns mockk()
        every { getAssociationIdUseCase() } returns null
        useCase()
        coVerify { sendNotificationTokenUseCase("fcmToken") }
    }

    @Test
    fun invokeNoFcmToken() = runBlocking {
        val getFcmTokenUseCase = mockk<IGetFcmTokenUseCase>()
        val useCase = SetupNotificationsUseCase(getFcmTokenUseCase, mockk(), mockk(), mockk())
        every { getFcmTokenUseCase() } returns null
        useCase()
    }

    @Test
    fun invokeThrowError() = runBlocking {
        val getFcmTokenUseCase = mockk<IGetFcmTokenUseCase>()
        val getAssociationIdUseCase = mockk<IGetAssociationIdUseCase>()
        val sendNotificationTokenUseCase = mockk<ISendNotificationTokenUseCase>()
        val useCase = SetupNotificationsUseCase(
            getFcmTokenUseCase, getAssociationIdUseCase, mockk(), sendNotificationTokenUseCase
        )
        every { getFcmTokenUseCase() } returns "fcmToken"
        coEvery { sendNotificationTokenUseCase("fcmToken") } throws Exception()
        useCase() // Test that exception is caught
        coVerify { sendNotificationTokenUseCase("fcmToken") }
    }

}
