package me.nathanfallet.suitebde.usecases.notifications

import me.nathanfallet.suitebde.repositories.application.ITokenRepository

class GetFcmTokenUseCase(
    private val tokenRepository: ITokenRepository,
) : IGetFcmTokenUseCase {

    override fun invoke() = tokenRepository.getFcmToken()

}
