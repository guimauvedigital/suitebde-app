package com.suitebde.usecases.notifications

import com.suitebde.repositories.notifications.INotificationsRepository

class GetSubscribedToNotificationTopicUseCase(
    private val notificationsRepository: INotificationsRepository,
) : IGetSubscribedToNotificationTopicUseCase {

    override fun invoke(input: String): Boolean = notificationsRepository.isTopicSubscribed(input)

}
