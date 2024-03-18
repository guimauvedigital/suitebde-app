package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.repositories.application.ITokenRepository
import me.nathanfallet.suitebde.repositories.notifications.INotificationsRepository

class ClearFcmTokenUseCase(
    private val tokenRepository: ITokenRepository,
    private val notificationsRepository: INotificationsRepository,
) : IClearFcmTokenUseCase {

    override fun invoke() {
        tokenRepository.setFcmToken(null)
        notificationsRepository.clearSubscriptions()
    }

}
