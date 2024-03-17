package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.repositories.application.ITokenRepository

class SetupNotificationsUseCase(
    private val tokenRepository: ITokenRepository,
    private val sendNotificationTokenUseCase: ISendNotificationTokenUseCase,
) : ISetupNotificationsUseCase {

    override suspend fun invoke() {
        val fcmToken = tokenRepository.getFcmToken() ?: return
        sendNotificationTokenUseCase(fcmToken)

        // TODO: Subscribe to topics
    }

}
