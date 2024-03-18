package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.models.SubscribeToNotificationTopicType
import me.nathanfallet.suitebde.repositories.application.ITokenRepository
import me.nathanfallet.suitebde.usecases.auth.IGetAssociationIdUseCase

class SetupNotificationsUseCase(
    private val tokenRepository: ITokenRepository,
    private val getAssociationIdUseCase: IGetAssociationIdUseCase,
    private val subscribeToNotificationTopicUseCase: ISubscribeToNotificationTopicUseCase,
    private val sendNotificationTokenUseCase: ISendNotificationTokenUseCase,
) : ISetupNotificationsUseCase {

    override suspend fun invoke() {
        try {
            val fcmToken = tokenRepository.getFcmToken() ?: return
            sendNotificationTokenUseCase(fcmToken)

            val associationId = getAssociationIdUseCase() ?: return
            subscribeToNotificationTopicUseCase(
                "associations/$associationId",
                SubscribeToNotificationTopicType.SUBSCRIBE
            )
            subscribeToNotificationTopicUseCase(
                "associations/$associationId/events",
                SubscribeToNotificationTopicType.AS_SAVED
            )
        } catch (e: Exception) {
            // Here we might just be offline, so we don't want to crash the app
            e.printStackTrace()
        }
    }

}
