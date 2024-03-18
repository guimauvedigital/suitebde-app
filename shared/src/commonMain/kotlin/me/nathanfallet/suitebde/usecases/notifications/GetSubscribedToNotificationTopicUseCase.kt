package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.repositories.notifications.INotificationsRepository

class GetSubscribedToNotificationTopicUseCase(
    private val notificationsRepository: INotificationsRepository,
) : IGetSubscribedToNotificationTopicUseCase {

    override fun invoke(input: String): Boolean = notificationsRepository.isTopicSubscribed(input)

}
