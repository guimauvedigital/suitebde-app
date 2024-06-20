package com.suitebde.usecases.auth

import com.suitebde.repositories.application.ITokenRepository

class GetRefreshTokenUseCase(
    private val tokenRepository: ITokenRepository,
) : IGetRefreshTokenUseCase {

    override fun invoke() = tokenRepository.getRefreshToken()

}