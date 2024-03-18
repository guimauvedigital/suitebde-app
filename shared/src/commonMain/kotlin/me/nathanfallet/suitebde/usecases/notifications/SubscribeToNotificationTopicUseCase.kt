package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.notifications.SubscribeToNotificationTopicType
import me.nathanfallet.suitebde.repositories.notifications.INotificationsRepository

class SubscribeToNotificationTopicUseCase(
    private val notificationsRepository: INotificationsRepository,
) : ISubscribeToNotificationTopicUseCase {

    override fun invoke(input1: String, input2: SubscribeToNotificationTopicType) {
        val subscribe = when (input2) {
            SubscribeToNotificationTopicType.SUBSCRIBE -> true
            SubscribeToNotificationTopicType.UNSUBSCRIBE -> false
            SubscribeToNotificationTopicType.AS_SAVED -> notificationsRepository.isTopicSubscribed(input1)
        }
        if (subscribe) notificationsRepository.subscribeToTopic(input1)
        else notificationsRepository.unsubscribeFromTopic(input1)
    }

}
