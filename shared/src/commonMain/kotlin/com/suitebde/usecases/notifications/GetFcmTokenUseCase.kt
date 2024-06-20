package com.suitebde.usecases.notifications

import com.suitebde.repositories.application.ITokenRepository

class GetFcmTokenUseCase(
    private val tokenRepository: ITokenRepository,
) : IGetFcmTokenUseCase {

    override fun invoke() = tokenRepository.getFcmToken()

}
