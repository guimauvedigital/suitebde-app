package com.suitebde.usecases.notifications

import com.suitebde.repositories.application.ITokenRepository
import com.suitebde.repositories.notifications.INotificationsRepository

class ClearFcmTokenUseCase(
    private val tokenRepository: ITokenRepository,
    private val notificationsRepository: INotificationsRepository,
) : IClearFcmTokenUseCase {

    override fun invoke() {
        tokenRepository.setFcmToken(null)
        notificationsRepository.clearSubscriptions()
    }

}
