package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.repositories.application.ITokenRepository

class UpdateFcmTokenUseCase(
    private val tokenRepository: ITokenRepository,
    private val setupNotificationsUseCase: ISetupNotificationsUseCase,
) : IUpdateFcmTokenUseCase {

    override suspend fun invoke(input: String) {
        tokenRepository.setFcmToken(input)
        setupNotificationsUseCase()
    }

}
