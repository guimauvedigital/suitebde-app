package com.suitebde.usecases.notifications

import com.suitebde.models.notifications.SubscribeToNotificationTopicType
import com.suitebde.repositories.notifications.INotificationsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test

class SubscribeToNotificationTopicUseCaseTest {

    @Test
    fun testSubscribe() {
        val repository = mockk<INotificationsRepository>()
        val useCase = SubscribeToNotificationTopicUseCase(repository)
        every { repository.subscribeToTopic("topic") } returns Unit
        useCase.invoke("topic", SubscribeToNotificationTopicType.SUBSCRIBE)
        verify { repository.subscribeToTopic("topic") }
    }

    @Test
    fun testUnsubscribe() {
        val repository = mockk<INotificationsRepository>()
        val useCase = SubscribeToNotificationTopicUseCase(repository)
        every { repository.unsubscribeFromTopic("topic") } returns Unit
        useCase.invoke("topic", SubscribeToNotificationTopicType.UNSUBSCRIBE)
        verify { repository.unsubscribeFromTopic("topic") }
    }

    @Test
    fun testAsSavedSubscribed() {
        val repository = mockk<INotificationsRepository>()
        val useCase = SubscribeToNotificationTopicUseCase(repository)
        every { repository.isTopicSubscribed("topic") } returns true
        every { repository.subscribeToTopic("topic") } returns Unit
        useCase.invoke("topic", SubscribeToNotificationTopicType.AS_SAVED)
        verify { repository.subscribeToTopic("topic") }
    }

    @Test
    fun testAsSavedNotSubscribed() {
        val repository = mockk<INotificationsRepository>()
        val useCase = SubscribeToNotificationTopicUseCase(repository)
        every { repository.isTopicSubscribed("topic") } returns false
        every { repository.unsubscribeFromTopic("topic") } returns Unit
        useCase.invoke("topic", SubscribeToNotificationTopicType.AS_SAVED)
        verify { repository.unsubscribeFromTopic("topic") }
    }

}
