package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.repositories.application.ITokenRepository

class SetupNotificationsUseCase(
    private val tokenRepository: ITokenRepository,
    private val sendNotificationTokenUseCase: ISendNotificationTokenUseCase,
) : ISetupNotificationsUseCase {

    override suspend fun invoke() {
        try {
            val fcmToken = tokenRepository.getFcmToken() ?: return
            sendNotificationTokenUseCase(fcmToken)

            // TODO: Subscribe to topics
        } catch (e: Exception) {
            // Here we might just be offline, so we don't want to crash the app
            e.printStackTrace()
        }
    }

}
