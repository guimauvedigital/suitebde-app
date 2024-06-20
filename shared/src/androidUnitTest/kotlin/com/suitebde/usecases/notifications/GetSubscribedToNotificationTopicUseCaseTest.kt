package com.suitebde.usecases.notifications

import com.suitebde.repositories.notifications.INotificationsRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSubscribedToNotificationTopicUseCaseTest {

    @Test
    fun testInvokeTrue() {
        val repository = mockk<INotificationsRepository>()
        val useCase = GetSubscribedToNotificationTopicUseCase(repository)
        every { repository.isTopicSubscribed("test") } returns true
        assertEquals(true, useCase("test"))
    }

    @Test
    fun testInvokeFalse() {
        val repository = mockk<INotificationsRepository>()
        val useCase = GetSubscribedToNotificationTopicUseCase(repository)
        every { repository.isTopicSubscribed("test") } returns false
        assertEquals(false, useCase("test"))
    }

}
