package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.notifications.SubscribeToNotificationTopicType
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class SetupNotificationsUseCase(
    private val getFcmTokenUseCase: IGetFcmTokenUseCase,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
    private val subscribeToNotificationTopicUseCase: ISubscribeToNotificationTopicUseCase,
    private val sendNotificationTokenUseCase: ISendNotificationTokenUseCase,
) : ISetupNotificationsUseCase {

    override suspend fun invoke() {
        try {
            val fcmToken = getFcmTokenUseCase() ?: return
            sendNotificationTokenUseCase(fcmToken)

            val associationId = getAssociationIdUseCase() ?: return
            subscribeToNotificationTopicUseCase(
                "associations_$associationId",
                SubscribeToNotificationTopicType.SUBSCRIBE
            )
            subscribeToNotificationTopicUseCase(
                "associations_${associationId}_events",
                SubscribeToNotificationTopicType.AS_SAVED
            )
        } catch (e: Exception) {
            // Here we might just be offline, so we don't want to crash the app
            e.printStackTrace()
        }
    }

}
